package com.sappyoak.kotlinrl

import net.runelite.client.externalplugins.ExternalPluginManager
import net.runelite.client.RuneLite

import com.sappyoak.kotlinrl.plugins.blackjack.Blackjack

object Main {
    val defaultArgs = arrayOf("--developer-mode")

    @JvmStatic
    fun main(args: Array<String>) {
        ExternalPluginManager.loadBuiltin(Blackjack::class.java)
        RuneLite.main(defaultArgs)
    }
}