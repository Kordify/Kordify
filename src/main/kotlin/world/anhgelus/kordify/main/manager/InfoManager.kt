package world.anhgelus.kordify.main.manager

import org.yaml.snakeyaml.Yaml

object InfoManager {
    private val data: Map<String, Any?> = Yaml().load(javaClass.getResourceAsStream("info.yml")!!)

    fun get(key: String): Any? {
        return data[key]
    }

    fun getVersion(): String {
        return get("version")!! as String
    }

    fun getJdaVersion(): String {
        return get("jda-version")!! as String
    }
}