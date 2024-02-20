package world.anhgelus.kordify.common

import net.dv8tion.jda.api.JDA
import world.anhgelus.kordify.common.storage.Storage

object BotHelper {
    var instance: JDA? = null
        private set

    var storage: Storage? = null
        private set

    fun setInstance(bot: JDA) {
        if (instance != null) {
            return
        }
        instance = bot
    }

    fun setStorage(storage: Storage) {
        if (this.storage != null) {
            return
        }
        this.storage = storage
    }
}