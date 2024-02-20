package world.anhgelus.kordify

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.interactions.commands.build.Commands
import world.anhgelus.kordify.api.commands.CommandManager
import world.anhgelus.kordify.common.BotHelper
import world.anhgelus.kordify.common.config.Config
import world.anhgelus.kordify.common.utils.MainLogger
import world.anhgelus.kordify.main.plugins.PluginManager
import world.anhgelus.kordify.common.storage.CommandStorage
import world.anhgelus.kordify.common.storage.Storage
import java.io.File

fun main() {
    MainLogger.init()
    val logger = MainLogger.javaLogger
    logger.info("Starting")
    logger.info("Loading config file")

    val configFile = File("config.yml")
    if (configFile.createNewFile()) {
        logger.warning("File not found. Creating a new one.")
        configFile.writeText("token: \"\"\nintents:\n- MESSAGE_CONTENT\n- GUILD_MEMBERS\n- GUILD_PRESENCES")
        logger.info("Exiting")
        return
    }
    val config = Config.loadFromFile(configFile)

    logger.info("Loading plugins inside ./plugins")
    val manager = PluginManager()
    val loaded = manager.getPlugins().size
    logger.info("Loaded $loaded plugins")

    val builder = JDABuilder.createDefault(config.token)
        .addEventListeners(CommandManager)

    config.intents.forEach {
        builder.enableIntents(it)
    }
    val bot = builder.build()

    BotHelper.setInstance(bot)
    BotHelper.setStorage(Storage.loadFromFile("data.yml"))

    CommandManager.init()

    bot.awaitReady()

    logger.info("Starting plugins")
    val n = manager.startPlugins()
    logger.info("$n / $loaded plugins started")

    manager.getListeners().forEach {
        bot.addEventListener(it)
    }
}
