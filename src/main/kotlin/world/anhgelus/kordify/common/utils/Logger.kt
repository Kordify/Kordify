package world.anhgelus.kordify.common.utils

import java.util.logging.Logger

abstract class Logger {
    abstract val name: String
    lateinit var javaLogger: Logger
        private set

    fun init() {
        javaLogger = Logger.getLogger(name)
    }
}