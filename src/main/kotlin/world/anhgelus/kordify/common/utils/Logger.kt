package world.anhgelus.kordify.common.utils

import java.io.File
import java.util.*
import java.util.logging.ConsoleHandler
import java.util.logging.FileHandler
import java.util.logging.LogRecord
import java.util.logging.Logger

abstract class Logger {
    abstract val name: String
    lateinit var javaLogger: Logger
        private set

    private object Formatter : java.util.logging.Formatter() {
        override fun format(record: LogRecord?): String {
            return if (record?.thrown == null) {
                "[${record?.instant}] ${record?.level}: ${record?.message}\n"
            } else {
                "[${record.instant}] ${record.level}: ${record.message}\n${record.thrown}\n"
            }
        }
    }

    fun init() {
        javaLogger = Logger.getLogger(name)
        val cal = GregorianCalendar.getInstance()
        cal.time = Date()
        File("logs").mkdir()
        val fHandler = FileHandler(
            "logs/${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH+1)}-${cal.get(Calendar.DAY_OF_MONTH)+1}.log",
            1048576,
            5
        )
        fHandler.formatter = Formatter
        val cHandler = ConsoleHandler()
        cHandler.formatter = Formatter
        javaLogger.addHandler(fHandler)
        javaLogger.addHandler(cHandler)
    }

    fun info(msg: String) {
        javaLogger.info(msg)
    }

    fun warning(msg: String) {
        javaLogger.warning(msg)
    }

    fun severe(msg: String) {
        javaLogger.severe(msg)
    }
}