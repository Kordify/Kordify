package world.anhgelus.kordify

import world.anhgelus.kordify.common.utils.Logger
import world.anhgelus.kordify.main.plugins.PluginManager

fun main() {
    Logger.info("Starting")
    Logger.info("Loading plugins inside ./plugins")
    val manager = PluginManager()
    val loaded = manager.getPlugins().size
    Logger.info("Loaded $loaded plugins")
    Logger.info("Loading the bot")
    //TODO: load the bot
    Logger.info("Starting plugins")
    val n = manager.startPlugins()
    Logger.info("$n / $loaded plugins started")
    //TODO: start the bot
}
