package world.anhgelus.kordify.api.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import world.anhgelus.kordify.common.manager.CommandManager

abstract class Command {
    abstract val name: String
    abstract val description: String

    protected val permissions: MutableList<Permission> = ArrayList()
    protected val options = ArrayList<OptionData>()

    abstract fun handle(event: SlashCommandInteractionEvent)

    fun register() {
        CommandManager.register(this)
    }

    fun addOption(opt: OptionData): Command {
        options.add(opt)
        return this
    }

    fun getOptions(): List<OptionData> {
        return options
    }

    fun addPermission(p: Permission): Command {
        permissions.add(p)
        return this
    }

    fun getPermissions(): DefaultMemberPermissions {
        if (permissions.size == 0) {
            return DefaultMemberPermissions.DISABLED
        }
        return DefaultMemberPermissions.enabledFor(permissions)
    }

    fun toJDACommand(): SlashCommandData {
        return Commands.slash(name, description)
            .addOptions(options)
    }
}