package world.anhgelus.kordify.main

import net.dv8tion.jda.api.requests.GatewayIntent
import world.anhgelus.kordify.common.config.Config

/**
 * Manage intents
 */
object IntentsManager {
    /**
     * Generate intents required
     * @param manager the plugin manager
     * @param config the config used
     * @return list of intents required
     */
    fun generateIntents(manager: PluginManager, config: Config): List<GatewayIntent> {
        val intents = config.intents as MutableList<GatewayIntent>
        manager.getPlugins().forEach { p ->
            p.intents?.forEach {
                if (it !in intents) {
                    intents.add(it)
                }
            }
        }
        return intents
    }
}