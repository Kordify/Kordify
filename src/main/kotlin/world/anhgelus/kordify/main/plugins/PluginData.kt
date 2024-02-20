package world.anhgelus.kordify.main.plugins

/**
 * Represents a plugin
 * @param fileName file of the plugin
 * @param mainClass main class of the plugin
 * @param author author of the plugin
 * @param version version of the plugin
 */
data class PluginData(
    val fileName: String,
    val mainClass: String,
    val author: String,
    val version: String,
)