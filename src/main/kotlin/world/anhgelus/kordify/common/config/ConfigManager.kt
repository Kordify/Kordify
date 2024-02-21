package world.anhgelus.kordify.common.config

import org.yaml.snakeyaml.Yaml
import java.io.File

/**
 * Help managing the config
 * @param pluginName name of the plugin
 */
class ConfigManager(
    private val pluginName: String
) {
    /**
     * Get or create the config with the given path
     * @param path the path
     * @return the config
     */
    fun getConfig(path: String): ConfigFile {
        val yaml = Yaml()
        val fullPath = generateFullPath(path)
        createDirs(path)
        val file = File(fullPath)
        if (file.createNewFile()) {
            return ConfigFile(HashMap(), fullPath)
        }
        val map: MutableMap<String, Any> = yaml.load(file.inputStream())
        return ConfigFile(map, fullPath)
    }

    /**
     * Generate the full path
     * @param path the path to use
     * @return the full path
     */
    private fun generateFullPath(path: String): String {
        return "plugins/$pluginName/$path.yml"
    }

    private fun createDirs(path: String): Boolean {
        val paths = path.split("/")
        val file = File(paths.subList(0,paths.size-1).joinToString(separator = "/"))
        return file.mkdirs()
    }
}