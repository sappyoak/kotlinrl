package com.sappyoak.shared

import java.net.URL
import java.nio.charset.StandardCharsets

private const val GAME_PROPS_URL: String = "https://oldschool.runescape.com/jav_config.ws"

data class GameProperties(
    private val appletProperties: Map<String, String>,
    private val classLoaderProperties: Map<String, String>
) {
    operator fun get(key: String): String = getOrNull(key) ?: error("Could not load '$key' from GameProperties. it was not found")
    fun getOrNull(key: String): String? = appletProperties[key] ?: classLoaderProperties[key]

    val revision: String get() = get("25")
    val initialJar: String get() = get("initial_jar")
    val initialClass: String get() = get("initial_class")
}

fun downloadGameProperties(url: String = GAME_PROPS_URL): GameProperties {
    val appletProperties = hashMapOf<String, String>()
    val classLoaderProperties = hashMapOf<String, String>()

    URL(url).openStream().bufferedReader(StandardCharsets.ISO_8859_1)
        .lineSequence()
        .forEach { line ->
            val (key, value) = line.split("=", limit = 2).toTypedArray()
            when (key) {
                "param" -> {
                    val (paramKey, paramValue) = value.split("=", limit = 2).toTypedArray()
                    appletProperties[paramKey] = paramValue
                }
                "msg" -> {}
                else -> classLoaderProperties[key] = value
            }
        }

    return GameProperties(appletProperties, classLoaderProperties)
}
