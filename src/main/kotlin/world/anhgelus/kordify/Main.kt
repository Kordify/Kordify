package world.anhgelus.kordify

import world.anhgelus.kordify.common.utils.MainLogger
import world.anhgelus.kordify.main.plugins.PluginManager

fun main() {
    MainLogger.info("Starting")

    MainLogger.info("Loading plugins inside ./plugins")
    val manager = PluginManager()
    val loaded = manager.getPlugins().size
    MainLogger.info("Loaded $loaded plugins")

    MainLogger.info("Loading the bot")
    //TODO: load the bot

    MainLogger.info("Starting plugins")
    val n = manager.startPlugins()
    MainLogger.info("$n / $loaded plugins started")

    //TODO: start the bot
}
