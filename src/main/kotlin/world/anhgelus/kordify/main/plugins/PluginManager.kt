package world.anhgelus.kordify.main.plugins

import world.anhgelus.kordify.common.utils.Logger
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * Manage plugins
 */
class PluginManager {

    private val plugins: MutableList<PluginData> = ArrayList()

    init {
        listPlugins()
    }

    /**
     * Generate the list of plugins
     */
    private fun listPlugins() {
        val result: MutableList<File> = ArrayList()

        Files.newDirectoryStream(Paths.get("./plugins")).use { stream ->
            for (file in stream) {
                if (file != null && file.fileName.toString().endsWith(".jar")) {
                    result.add(file.toFile())
                }
            }
        }

        result.forEach {
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
                Logger.info(jarFile.name+" is not a valid plugin")
            } else {
                val ins = jarFile.getInputStream(conf)
                //TODO: parse conf
                plugins.add(PluginData(jarFile.name, "", "", ""))
            }
        }
    }

    /**
     * Start every plugin
     * @return the number of plugin started
     */
    fun startPlugins() : Int {
        //TODO: start plugins
        return 0
    }

    /**
     * Get the plugins
     * @return list of loaded plugins
     */
    fun getPlugins() : List<PluginData> {
        return plugins
    }
}