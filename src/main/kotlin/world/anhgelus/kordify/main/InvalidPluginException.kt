package world.anhgelus.kordify.main

class InvalidPluginException(
    val reason: Reason,
    override val message: String
) : Exception() {
    enum class Reason(
        val message: String
    ) {
        INVALID_PLUGIN_YML("Invalid plugin.yml")
    }

    override fun toString(): String {
        return "${reason.message} ($message)"
    }
}