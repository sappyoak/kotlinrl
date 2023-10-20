package com.sappyoak.shared

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths

object Files {
    val RuneLiteRoot = File(System.getProperty("user.name"), ".runelite")

    val Root: File by lazy {
        File(System.getProperty("user.name"), ".kotlinrl").also { it.mkdirs() }
    }

    // Separate logging directory from RuneLite to make use of our internal logging
    // to avoid leaking naughty information to RL through any logging telemetry
    val Logs: File by lazy {
        File(Root, "logs").also { it.mkdirs() }
    }

    val Plugins: File by lazy {
        File(Root, "plugins").also { it.mkdirs() }
    }

    val CurrentRevision: File by lazy {
        File(Root, "current-revision.txt")
    }

    fun loadCurrentRevision(): String {
        if (CurrentRevision.exists()) {
            return CurrentRevision.readText(StandardCharsets.UTF_8).trim()
        }

        val gameProperties = downloadGameProperties()
        val revisionText = gameProperties.revision

        CurrentRevision.writeText(revisionText, StandardCharsets.UTF_8)
        return revisionText
    }
}
