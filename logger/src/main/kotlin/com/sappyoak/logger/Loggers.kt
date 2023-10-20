package com.sappyoak.logger

class DefaultConsoleLogger(override val name: String, initialLevel: LogLevel = LogLevel.Info) : Logger {
    private var _currentLevel: LogLevel = initialLevel
    override val currentLevel: LogLevel = _currentLevel

    override fun log(level: LogLevel, message: String) {
        println("[$level]: $message")
    }

    override fun setLogLevel(level: LogLevel) {
        _currentLevel = level
    }
}

class TaggedLogger(private val tag: String, private val delegate: Logger) : Logger by delegate {
    override fun log(level: LogLevel, message: String) {
        delegate.log(level, "[$tag] - $message")
    }
}