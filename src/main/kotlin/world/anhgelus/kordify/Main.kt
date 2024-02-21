package world.anhgelus.kordify

import net.dv8tion.jda.api.JDABuilder
import world.anhgelus.kordify.common.manager.CommandManager
import world.anhgelus.kordify.common.BotHelper
import world.anhgelus.kordify.common.config.Config
import world.anhgelus.kordify.main.IntentsManager
import world.anhgelus.kordify.main.MainLogger
import world.anhgelus.kordify.main.PluginManager
import world.anhgelus.kordify.main.storage.Storage
import java.io.File

fun main() {
    MainLogger.init()
    MainLogger.info("Starting")
    MainLogger.info("Loading config file")

    BotHelper.state = BotHelper.State.LOADING

    val configFile = File("config.yml")
    if (configFile.createNewFile()) {
        MainLogger.warning("File not found. Creating a new one.")
        configFile.writeText("token: \"\"\nintents:\n  - MESSAGE_CONTENT\n  - GUILD_MEMBERS\n  - GUILD_PRESENCES")
        MainLogger.info("Exiting")
        return
    }
    val config = Config.loadFromFile(configFile)

    MainLogger.info("Loading plugins inside ./plugins")
    val manager = PluginManager()
    val loaded = manager.getPlugins().size
    MainLogger.info("Loaded $loaded plugins")

    val bot = JDABuilder.createDefault(config.token)
        .addEventListeners(CommandManager)
        .enableIntents(IntentsManager.generateIntents(manager, config))
        .build()

    BotHelper.setInstance(bot)
    BotHelper.setStorage(Storage.loadFromFile("data.yml"))

    CommandManager.init()

    bot.awaitReady()

    BotHelper.state = BotHelper.State.LAUNCHING_PLUGINS

    MainLogger.info("Starting plugins")
    val n = manager.startPlugins()
    MainLogger.info("$n / $loaded plugins started")

    manager.getListeners().forEach {
        bot.addEventListener(it)
    }

    BotHelper.state = BotHelper.State.LISTENERS_REGISTERED

    bot.awaitShutdown()

    BotHelper.state = BotHelper.State.STOPPING_PLUGINS

    manager.stopPlugins()

    BotHelper.state = BotHelper.State.NOT_LOADED
}
