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
     * Get the config with the given path
     * @param path the path
     * @return the config
     */
    fun getConfig(path: String): ConfigFile {
        val yaml = Yaml()
        val fullPath = generateFullPath(path)
        val map: MutableMap<String, Any> = yaml.load(File(fullPath).inputStream())
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
}