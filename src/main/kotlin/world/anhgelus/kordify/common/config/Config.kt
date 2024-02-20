package world.anhgelus.kordify.common.config

import net.dv8tion.jda.api.requests.GatewayIntent
import org.yaml.snakeyaml.Yaml
import java.io.File

data class Config(
    val token: String,
    val intents: List<GatewayIntent>
) {
    companion object {
        fun loadFromFile(file: File) : Config {
            val yaml = Yaml()
            val m: Map<String, Any> = yaml.load(file.inputStream())
            val token = m["token"] as String
            val i = m["intents"] as List<*>
            val intents = ArrayList<GatewayIntent>()
            i.forEach {
                intents.add(GatewayIntent.valueOf(it.toString()))
            }
            return Config(token, intents)
        }
    }
}
