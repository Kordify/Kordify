package world.anhgelus.kordify.common.utils

import java.util.logging.Logger

object Logger {
    private val javaLogger = Logger.getLogger("Kordify")
    fun info(msg: String) {
        javaLogger.info(msg)
    }
}