package world.anhgelus.kordify.main

import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.yaml.snakeyaml.Yaml
import world.anhgelus.kordify.api.Plugin
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.io.path.exists

/**
 * Manage plugins
 *
 * @throws InvalidPluginException if one or more plugin is invalid
 */
class PluginManager {

    private val plugins: MutableList<PluginData> = ArrayList()
    private val pluginsStarted: MutableList<Plugin> = ArrayList()
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
             * @throws InvalidPluginException if the map is invalid
             */
            fun loadFromMap(m: Map<String, Any>, filename: String) : PluginData {
                val mainClass = m["main"] as String?
                    ?: throw InvalidPluginException(InvalidPluginException.Reason.INVALID_PLUGIN_YML, "main is not present")
                val author = m["author"] as String?
                    ?: throw InvalidPluginException(InvalidPluginException.Reason.INVALID_PLUGIN_YML, "author is not present")
                val version = m["version"] as String?
                    ?: throw InvalidPluginException(InvalidPluginException.Reason.INVALID_PLUGIN_YML, "version is not present")
                val name = m["name"] as String?
                    ?: throw InvalidPluginException(InvalidPluginException.Reason.INVALID_PLUGIN_YML, "name is not present")
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

        result.forEach {
            try {
                loadPlugin(it)
            } catch (e: InvalidPluginException) {
                MainLogger.warning("Error while loading ${it.name} : $e")
            }
        }
    }

    /**
     * Load a plugin from a jar file
     * @param file the jar file to use
     * @throws InvalidPluginException if the plugin is invalid
     */
    private fun loadPlugin(file: File) {
        val jar = JarFile(file)
        val conf: JarEntry = jar.getJarEntry("plugin.yml")
            ?: throw InvalidPluginException(InvalidPluginException.Reason.INVALID_PLUGIN_YML, "not found")

        val ins = jar.getInputStream(conf)
            ?: throw InvalidPluginException(InvalidPluginException.Reason.INVALID_PLUGIN_YML, "empty")
        val yaml = Yaml()
        val m: Map<String, Any> = yaml.load(ins)
        val data = PluginData.loadFromMap(m, jar.name)
        plugins.add(data)
        jar.close()
        ins.close()
    }

    /**
     * Start every plugin
     * @return the number of plugin started
     */
    fun startPlugins() : Int {
        var c = 0
        for (p in plugins) {
            try {
                val loader = URLClassLoader.newInstance(arrayOf(URL("jar:file:${p.filename}!/")))
                val pl = loader.loadClass(p.mainClass).getDeclaredConstructor().newInstance() as Plugin
                listeners.addAll(pl.listeners)
                pl.setPluginName(p.name)
                pl.start()
                pluginsStarted.add(pl)
                c++
            } catch (e: Exception) {
                MainLogger.severe("Error while loading ${p.filename}")
                e.printStackTrace()
            }
        }
        return c
    }

    /**
     * Stop every plugin
     */
    fun stopPlugins() {
        for (p in pluginsStarted) {
            p.stop()
        }
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