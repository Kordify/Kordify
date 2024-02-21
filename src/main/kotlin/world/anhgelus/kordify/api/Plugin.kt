package world.anhgelus.kordify.api

import net.dv8tion.jda.api.hooks.ListenerAdapter
import world.anhgelus.kordify.common.BotHelper
import world.anhgelus.kordify.common.BotState
import world.anhgelus.kordify.common.config.ConfigManager
import world.anhgelus.kordify.common.utils.Logger

/**
 * Interface of the plugin
 */
abstract class Plugin {
    /**
     * Name of the plugin
     *
     * Do not edit!
     */
    private var pluginName: String? = null

    /**
     * Logger of the plugin
     */
    abstract val logger: Logger

    /**
     * Listeners.
     *
     * Do not register a listener manually! Use {@link #registerListener(ListenerAdapter) registerListener} instead!
     */
    val listeners: MutableList<ListenerAdapter> = ArrayList()

    /**
     * Function called when the plugin is starting
     */
    abstract fun start()

    /**
     * Register a listener.
     *
     * @param listener listener to register
     */
    fun registerListener(listener: ListenerAdapter) {
        if (BotHelper.state == BotState.LISTENERS_REGISTERED) {
            BotHelper.instance!!.addEventListener(listener)
        }
        listeners.add(listener)
    }

    /**
     * Get the config manager
     *
     * @return the config manager
     */
    fun getConfigManager(): ConfigManager {
        return ConfigManager(pluginName!!)
    }

    /**
     * Set the plugin name.
     *
     * Do not call it! It is already called automatically by Kordify.
     *
     * @throws IllegalArgumentException if the method is called twice
     * @param name name of the plugin
     */
    fun setPluginName(name: String) {
        if (pluginName != null) {
            throw IllegalArgumentException("The plugin name was already set")
        }
        pluginName = name
    }
}