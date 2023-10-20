package com.sappyoak.logger

typealias MessageProducer = () -> String

interface Logger {
    val name: String
    val currentLevel: LogLevel

    fun log(level: LogLevel, message: String)
    fun logIfEnabled(level: LogLevel, message: String, throwable: Throwable? = null) {
        if (currentLevel.isEnabled(level)) {
            log(level, "$message\n ${throwable?.let { it.printStackTrace() } ?: ""}")
        }
    }

    fun verbose(message: String) { logIfEnabled(LogLevel.Verbose, message) }
    fun debug(message: String) { logIfEnabled(LogLevel.Debug, message) }
    fun trace(message: String) { logIfEnabled(LogLevel.Trace, message) }
    fun info(message: String) { logIfEnabled(LogLevel.Info, message) }

    fun warn(message: String) { logIfEnabled(LogLevel.Warn, message) }
    fun warn(message: String, throwable: Throwable) {
        logIfEnabled(LogLevel.Warn, message, throwable)
    }

    fun error(message: String) { logIfEnabled(LogLevel.Error, message) }
    fun error(message: String, throwable: Throwable) {
        logIfEnabled(LogLevel.Error, message, throwable)
    }

    fun setLogLevel(level: LogLevel)
}

inline fun Logger.logIfEnabled(level: LogLevel, message: MessageProducer, throwable: Throwable? = null) {
    if (currentLevel.isEnabled(level)) {
        log(level, "${message()}\n ${throwable?.let { it.printStackTrace() } ?: ""}")
    }
}

inline fun Logger.verbose(message: MessageProducer) { logIfEnabled(LogLevel.Verbose, message) }
inline fun Logger.debug(message: MessageProducer) { logIfEnabled(LogLevel.Debug, message) }
inline fun Logger.trace(message: MessageProducer) { logIfEnabled(LogLevel.Trace, message) }
inline fun Logger.info(message: MessageProducer) { logIfEnabled(LogLevel.Info, message) }

inline fun Logger.warn(message: MessageProducer) { logIfEnabled(LogLevel.Warn, message) }
inline fun Logger.warn(throwable: Throwable, message: MessageProducer) { logIfEnabled(LogLevel.Warn, message, throwable) }

inline fun Logger.error(message: MessageProducer) { logIfEnabled(LogLevel.Error, message) }
inline fun Logger.error(throwable: Throwable, message: MessageProducer) { logIfEnabled(LogLevel.Error, message, throwable) }
