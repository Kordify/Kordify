package world.anhgelus.kordify.common.config

import org.yaml.snakeyaml.Yaml
import java.io.File

/**
 * Representation of a config file
 *
 * @param map map returned by SnakeYaml
 */
class ConfigFile(
    private val map: MutableMap<String, Any>,
    private val path: String,
) {
    /**
     * Save the content of the config file
     */
    fun save() {
        val file = File(path)
        file.createNewFile()
        val yaml = Yaml()
        file.writeText(yaml.dump(map))
    }

    /**
     * Set a value at the key
     *
     * @param key key to use
     * @param value value to store
     * @return the config file used
     */
    fun set(key: String, value: Any): ConfigFile {
        val uris = key.split(".")
        var last: MutableMap<Any?, Any?> = map.toMap(HashMap())
        for (uri in uris.subList(0,uris.size-1)) {
            val star = map[uri] as MutableMap<*, *>
            last = star.toMap(HashMap())
        }
        last[uris.last()] = value
        return this
    }

    /**
     * Get the object at the path
     * @param path path of the object
     * @return the object
     */
    fun get(path: String): Any? {
        val uris = path.split(".")
        var last: Map<*, *> = map
        for (uri in uris.subList(0,uris.size-1)) {
            last = map[uri] as Map<*, *>
        }
        return last[uris.last()]
    }

    /**
     * Get the string at the path
     * @param path path of the string
     * @return the string
     */
    fun getString(path: String): String {
        return get(path)!! as String
    }

    /**
     * Get the bool at the path
     * @param path path of the bool
     * @return the bool
     */
    fun getBool(path: String): Boolean {
        return get(path)!! as Boolean
    }

    /**
     * Get the int at the path
     * @param path path of the int
     * @return the int
     */
    fun getInt(path: String): Int {
        return get(path)!! as Int
    }

    /**
     * Get the long at the path
     * @param path path of the long
     * @return the long
     */
    fun getLong(path: String): Long {
        return get(path)!! as Long
    }

    /**
     * Get the float at the path
     * @param path path of the float
     * @return the float
     */
    fun getFloat(path: String): Float {
        return get(path)!! as Float
    }

    /**
     * Get the double at the path
     * @param path path of the double
     * @return the double
     */
    fun getDouble(path: String): Double {
        return get(path)!! as Double
    }
}