package world.anhgelus.kordify.main.manager

import net.dv8tion.jda.api.requests.GatewayIntent
import world.anhgelus.kordify.common.config.Config
import world.anhgelus.kordify.main.MainLogger

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
        val enabled = config.intents.toMutableSet()
        // all intents not in DEFAULT
        val max = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS and GatewayIntent.DEFAULT.inv())
        if (enabled == max) return enabled
        for (p in manager.getPlugins()) {
            if (max == intents) break
            p.intents?.forEach {
                // if intent is not in DEFAULT
                if (it.rawValue and GatewayIntent.DEFAULT == 0) {
                    intents.add(it)
                }
            }
        }
        if (enabled != intents) {
            // intents not in enabled
            val diff = GatewayIntent.getIntents(GatewayIntent.getRaw(intents) and GatewayIntent.getRaw(enabled).inv())
            MainLogger.warning("You have to enable these intents if you want to work properly with all installed" +
                    "plugins : ${diff.joinToString(separator = ", ")}")
            return enabled
        }
        return intents
    }
}