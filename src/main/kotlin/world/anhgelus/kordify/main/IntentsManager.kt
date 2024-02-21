package world.anhgelus.kordify.main

import net.dv8tion.jda.api.requests.GatewayIntent
import world.anhgelus.kordify.common.config.Config
import java.util.EnumSet

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
    fun generateIntents(manager: PluginManager, config: Config): Set<GatewayIntent> {
        val intents = config.intents.toMutableSet()
        val max = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS and GatewayIntent.DEFAULT.inv())
        for (p in manager.getPlugins()) {
            if (max == intents) return intents
            p.intents?.forEach { intents.add(it) }
        }
        return intents
    }
}