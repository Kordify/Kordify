package world.anhgelus.kordify.main.storage

import org.yaml.snakeyaml.Yaml
import world.anhgelus.kordify.api.commands.Command
import java.io.File

/**
 * Storage
 * @param path path of the storage
 * @param commands Storage of the commands
 */
data class Storage(
    val path: String,
    var commands: MutableList<CommandStorage>,
) {
    /**
     * Turns the storage in a map
     * @return the map
     */
    fun toMap() : MutableMap<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        val cmd: MutableList<Map<String, Any>> = ArrayList()
        commands.forEach {
            cmd.add(it.toMap())
        }
        map["commands"] = cmd
        return map
    }

    /**
     * Save the storage in the path
     */
    fun save() {
        val yaml = Yaml()
        val dump = yaml.dump(toMap())
        val file = File(path)
        file.createNewFile()
        file.writeText(dump)
    }

    companion object {
        /**
         * Load the storage from a file
         * @param path path to load form
         * @return the storage generated
         */
        fun loadFromFile(path: String) : Storage {
            val file = File(path)
            if (!file.exists() || !file.isFile) {
                val s = Storage(path, ArrayList())
                s.save()
                return s
            }
            val yaml = Yaml()
            val map: MutableMap<String, Any> = yaml.load(file.inputStream())
            val cmd = map["commands"] as MutableList<*>
            val commands = ArrayList<CommandStorage>()
            cmd.forEach {
                val data = it as Map<*, *>
                commands.add(CommandStorage.fromMap(data))
            }
            return Storage(path, commands)
        }
    }
}

/**
 * Storage of the command
 * @param name name of the command
 * @param description description of the command
 * @param permission permission of the command
 */
data class CommandStorage(
    val name: String,
    val description: String,
    val permission: Long,
) {
    /**
     * Turns the storage into a map
     * @return the map
     */
    fun toMap() : MutableMap<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["name"] = name
        map["description"] = description
        map["perms"] = permission
        return map
    }

    companion object {
        /**
         * Load the command storage from a map
         * @param map map to load from
         * @return command storage generated
         */
        fun fromMap(map: Map<*, *>): CommandStorage {
            val name = map["name"] as String
            val description = map["description"] as String
            return when (val perms = map["perms"]) {
                is Int -> {
                    CommandStorage(name, description, perms.toLong())
                }

                is Long -> {
                    CommandStorage(name, description, perms)
                }

                else -> {
                    throw IllegalArgumentException("Perms does not have a valid value")
                }
            }
        }

        /**
         * Load the command storage from a command
         * @param c command to load from
         * @return command storage generated
         */
        fun fromCommand(c: Command): CommandStorage {
            return CommandStorage(c.name, c.description, c.getPermissions().permissionsRaw!!)
        }
    }
}
