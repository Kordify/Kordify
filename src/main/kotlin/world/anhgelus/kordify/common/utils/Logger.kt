package world.anhgelus.kordify.common.utils

import java.util.logging.Logger

abstract class Logger {
    abstract val name: String
    val javaLogger = Logger.getLogger(name)

    fun info(msg: String) {
        javaLogger.info(msg)
    }

    fun warn(msg: String) {
        javaLogger.warning(msg)
    }

    fun error(msg: String) {
        javaLogger.severe(msg)
    }
}