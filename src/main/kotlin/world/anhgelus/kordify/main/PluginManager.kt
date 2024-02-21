package world.anhgelus.kordify.main

import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.yaml.snakeyaml.Yaml
import world.anhgelus.kordify.api.Plugin
import world.anhgelus.kordify.common.utils.MainLogger
import java.io.File
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.io.path.exists

/**
 * Manage plugins
 */
class PluginManager {

    private val plugins: MutableList<PluginData> = ArrayList()
    private val listeners: MutableList<ListenerAdapter> = ArrayList()

    init {
        listPlugins()
    }

    /**
     * Represents a plugin
     * @param filename file of the plugin
     * @param mainClass main class of the plugin
     * @param author author of the plugin
     * @param version version of the plugin
     */
    data class PluginData(
        val filename: String,
        val name: String,
        val mainClass: String,
        val author: String,
        val version: String,
    ) {
        companion object {
            /**
             * Load plugin data from a map
             * @param m the map to use
             * @return the plugin data or null if the map is incomplete
             */
            fun loadFromMap(m: Map<String, Any>, filename: String) : PluginData? {
                val mainClass = m["main"] as String? ?: return null
                val author = m["author"] as String? ?: return null
                val version = m["version"] as String? ?: return null
                val name = m["name"] as String? ?: return null
                return PluginData(filename, name, mainClass, author, version)
            }
        }
    }

    /**
     * Generate the list of plugins
     */
    private fun listPlugins() {
        val result: MutableList<File> = ArrayList()

        val path = Paths.get("./plugins")
        if (!path.exists()) {
            path.toFile().mkdir()
        }

        Files.newDirectoryStream(path).use { stream ->
            for (file in stream) {
                if (file != null && file.fileName.toString().endsWith(".jar")) {
                    result.add(file.toFile())
                }
            }
        }

        for (it in result) {
            val jarFile = JarFile(it)
            val e = jarFile.entries()
            var conf: JarEntry? = null
            while (e.hasMoreElements() && conf == null) {
                val je = e.nextElement()
                if (je.name == "plugin.yml") {
                    conf = je
                }
            }
            if (conf == null) {
                MainLogger.javaLogger.warning(jarFile.name+" is not a valid plugin (plugin.yml not found)")
                continue
            }

            val ins = jarFile.getInputStream(conf)
            if (ins == null) {
                MainLogger.javaLogger.warning(jarFile.name+" is not a valid plugin (plugin.yml is empty)")
                continue
            }
            val yaml = Yaml()
            val m: Map<String, Any> = yaml.load(ins)
            val data = PluginData.loadFromMap(m, jarFile.name)
            if (data == null) {
                MainLogger.javaLogger.warning(jarFile.name+" is not a valid plugin (missing information in plugin.yml)")
                continue
            }
            plugins.add(data)
        }
    }

    /**
     * Start every plugin
     * @return the number of plugin started
     */
    fun startPlugins() : Int {
        var c = 0
        for (p in plugins) {
            try {
                val f = File("plugins/${p.filename}")
                val loader = URLClassLoader.newInstance(arrayOf(f.toURI().toURL()))
                val pl = loader.loadClass(p.mainClass).getDeclaredConstructor().newInstance() as Plugin
                listeners.addAll(pl.listeners)
                pl.setPluginName(p.name)
                pl.start()
                c++
            } catch (e: Exception) {
                MainLogger.javaLogger.severe("Error while loading ${p.filename}")
                e.printStackTrace()
            }
        }
        return c
    }

    /**
     * Get the plugins
     * @return list of loaded plugins
     */
    fun getPlugins() : List<PluginData> {
        return plugins
    }

    /**
     * Get the listeners
     * @return list of listeners
     */
    fun getListeners() : List<ListenerAdapter> {
        return listeners
    }
}