package world.anhgelus.kordify.api

import world.anhgelus.kordify.common.utils.Logger

/**
 * Interface of the plugin
 */
interface Plugin {
    /**
     * Logger of the plugin
     */
    val logger: Logger

    /**
     * Function called when the plugin is starting
     */
    fun start()
}