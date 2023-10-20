package com.sappyoak.kotlinrl.plugins.blackjack

import javax.inject.Inject
import net.runelite.api.ChatMessageType
import net.runelite.api.Client
import net.runelite.api.GameState
import net.runelite.api.MenuEntry
import net.runelite.api.events.ChatMessage
import net.runelite.api.events.ClientTick
import net.runelite.api.events.GameTick
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.Plugin
import net.runelite.client.plugins.PluginDescriptor
import net.runelite.client.util.Text
import kotlin.random.Random
import kotlin.random.nextInt

import com.sappyoak.kotlinrl.data.Region
import com.sappyoak.kotlinrl.data.isInRegion

@PluginDescriptor(
    name = "blackjack",
    enabledByDefault = true
)
class Blackjack : Plugin() {
    @Inject
    lateinit var client: Client

    private var nextKnockoutTick: Int = 0
    private var targetAwake: Boolean = true


    override fun startUp() {}
    override fun shutDown() {}

    @Subscribe
    fun onClientTick(event: ClientTick) {
        if (client.gameState != GameState.LOGGED_IN || !client.isInRegion(Region.Pollnivneach)) return

        val entries: Array<MenuEntry> = client.menuEntries
        var indexToMoveToTop = -1

        for ((index, entry) in entries.withIndex()) {
            val target = entry.target.removeTags()
            val option = entry.option.removeTags()

            if (isValidTarget(target)) {
                if ((option == EntryOptions.Knockout && targetAwake) || (option == EntryOptions.Pickpocket && !targetAwake)) {
                    indexToMoveToTop = index
                    break
                }
            }
        }

        if (indexToMoveToTop != -1) {
            val lastEntry = entries.last()
            entries[entries.size - 1] = entries[indexToMoveToTop]
            entries[indexToMoveToTop] = lastEntry
            client.menuEntries = entries
        }
    }

    @Subscribe
    fun onGameTick(event: GameTick) {
        targetAwake = client.tickCount >= nextKnockoutTick
    }

    @Subscribe
    fun onChatMessage(event: ChatMessage) {
        val message = event.message
        val type = event.type

        if (type == ChatMessageType.SPAM && (message == ChatMessages.Success) || message == ChatMessages.Failure) {
            targetAwake = false
            nextKnockoutTick = client.tickCount + Random.nextInt(3, 4)
        }
    }

    private fun String.removeTags(): String = Text.removeTags(this).lowercase()

    private fun isValidTarget(target: String): Boolean =
        target.contains(Targets.Bandit) || target.contains(Targets.Menaphite)


    object Targets {
        const val Bandit: String = "bandit"
        const val Menaphite: String = "menaphite thug"
    }

    object EntryOptions {
        const val Knockout: String = "knock-out"
        const val Pickpocket: String = "pickpocket"
    }

    object ChatMessages {
        const val Success: String = "You smack the bandit over the head and render them unconscious."
        const val Failure: String = "Your blow only glances off the bandit's head."
    }
}

