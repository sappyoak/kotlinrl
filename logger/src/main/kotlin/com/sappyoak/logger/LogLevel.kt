package com.sappyoak.logger

enum class LogLevel {
    Verbose,
    Debug,
    Trace,
    Info,
    Warn,
    Error,
    Off;
}

fun LogLevel.isEnabled(other: LogLevel): Boolean = other.ordinal >= ordinal
