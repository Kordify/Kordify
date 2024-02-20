package world.anhgelus.kordify.main.plugins

/**
 * Represents a plugin
 * @param filename file of the plugin
 * @param mainClass main class of the plugin
 * @param author author of the plugin
 * @param version version of the plugin
 */
data class PluginData(
    val filename: String,
    val mainClass: String,
    val author: String,
    val version: String,
) {
    companion object {
        fun loadFromMap(m: Map<String, Any>, filename: String) : PluginData {
            val mainClass = m["main"] as String
            val author = m["author"] as String
            val version = m["version"] as String
            return PluginData(filename, mainClass, author, version)
        }
    }
}