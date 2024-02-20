package world.anhgelus.kordify.api

import net.dv8tion.jda.api.hooks.ListenerAdapter
import world.anhgelus.kordify.common.utils.Logger

/**
 * Interface of the plugin
 */
interface Plugin {
    /**
     * Logger of the plugin
     */
    val logger: Logger
    val listeners: MutableList<ListenerAdapter>

    /**
     * Function called when the plugin is starting
     */
    fun start()
}