package world.anhgelus.kordify.common

import net.dv8tion.jda.api.JDA
import world.anhgelus.kordify.common.storage.Storage

object BotHelper {
    /**
     * Instance of the bot
     */
    var instance: JDA? = null
        private set

    /**
     * Global storage
     *
     * Do not edit!
     */
    var storage: Storage? = null
        private set

    /**
     * Bot state
     *
     * Do not edit!
     */
    var state: BotState = BotState.NOT_LOADED

    /**
     * Set the instance
     *
     * @param bot the instance to set
     * @return true if the instance was set, false otherwise
     */
    fun setInstance(bot: JDA): Boolean {
        if (instance != null) {
            return false
        }
        instance = bot
        return true
    }

    /**
     * Set the storage
     *
     * @param storage the storage
     * @return true if the storage was set, false otherwise
     */
    fun setStorage(storage: Storage): Boolean {
        if (this.storage != null) {
            return false
        }
        this.storage = storage
        return true
    }
}