package world.anhgelus.kordify.api.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import world.anhgelus.kordify.common.BotHelper
import world.anhgelus.kordify.common.storage.CommandStorage

/**
 * The command manager
 */
object CommandManager : ListenerAdapter() {
    private val registered = ArrayList<Command>()

    /**
     * Init the command manager
     */
    fun init() {
        HelpCommand.register()
    }

    /**
     * Register a command
     * @param c the command to register
     * @return true if the command was registered, false otherwise
     */
    fun register(c: Command): Boolean {
        // if command is already registered
        if (c in registered) {
            return false
        }

        val storage = BotHelper.storage!!
        val cs = CommandStorage.fromCommand(c)
        // if command was already registered previously
        if (cs in storage.commands) {
            registered.add(c)
            return false
        }

        val bot = BotHelper.instance!!
        bot.updateCommands().addCommands(c.toJDACommand()).queue()

        storage.commands.add(cs)
        storage.save()
        return true
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        registered.forEach {
            if (it.name == event.name) {
                it.handle(event)
            }
        }
    }

    /**
     * The generic help command (/help)
     */
    object HelpCommand : Command() {
        override val name = "help"
        override val description = "show the help"

        override fun handle(event: SlashCommandInteractionEvent) {
            val msg = StringBuilder()
            msg.append("Help\n\n")
            registered.forEach {
                msg.append("${it.name} - ${it.description}\n")
            }
            event.reply(msg.toString()).queue()
        }
    }
}