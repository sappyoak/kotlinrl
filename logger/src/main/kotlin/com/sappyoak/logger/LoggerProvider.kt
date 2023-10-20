package com.sappyoak.logger

fun getLogger(name: String, level: LogLevel = LogLevel.Info): Logger =
    DefaultConsoleLogger(name, level)

inline fun <reified T> getLogger(level: LogLevel = LogLevel.Info): Logger =
    getLogger(T::class.simpleName ?: "Anonymous", level)

fun getTaggedLogger(tag: String, name: String, level: LogLevel = LogLevel.Info): TaggedLogger =
    TaggedLogger(tag, DefaultConsoleLogger(name, level))

inline fun <reified T> getTaggedLogger(tag: String, level: LogLevel = LogLevel.Info): TaggedLogger =
    getTaggedLogger(tag, T::class.simpleName ?: "Anonymous", level)