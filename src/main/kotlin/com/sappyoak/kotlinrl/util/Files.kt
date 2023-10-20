package com.sappyoak.kotlinrl.util

import java.io.File

object Paths {
    val RuneLite: File = net.runelite.client.RuneLite.RUNELITE_DIR
    val Plugins: File = File(RuneLite.parentFile, "kotlinrl-plugins")
}
