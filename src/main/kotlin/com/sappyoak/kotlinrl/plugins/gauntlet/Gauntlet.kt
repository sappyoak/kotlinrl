package com.sappyoak.kotlinrl.plugins.gauntlet

import javax.inject.Inject
import net.runelite.api.Client
import net.runelite.api.GameState
import net.runelite.api.HeadIcon
import net.runelite.api.NPC
import net.runelite.api.NpcID
import net.runelite.api.Skill
import net.runelite.api.events.AnimationChanged
import net.runelite.api.events.GameTick
import net.runelite.api.events.MenuOptionClicked
import net.runelite.api.widgets.Widget
import net.runelite.api.widgets.WidgetInfo
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.game.ItemManager
import net.runelite.client.plugins.Plugin
import net.runelite.client.plugins.PluginDescriptor

import com.sappyoak.kotlinrl.data.player.Equipment
import com.sappyoak.kotlinrl.data.player.Inventory
import com.sappyoak.kotlinrl.packets.MousePackets
import com.sappyoak.kotlinrl.packets.WidgetPackets

private fun isQuickPrayerActive(): Boolean = false

@PluginDescriptor(
    name = "gauntlet",
    description = ""
)
class Gauntlet : Plugin() {
    @Inject
    lateinit var client: Client

    @Inject
    lateinit var itemManager: ItemManager
    var weapon: Weapon = Weapon.None

    var forceTab = false
    var isRanged = true

    val hunllefVarbitSet: Boolean
        get() = client.getVarbitValue(9177) == 1

    // Should cache the weapons the player has, and then only recheck during hunleff fight and not do a search
    // every single time
    @Subscribe
    fun onGameTick(event: GameTick) {
        if (client.localPlayer.isDead || client.localPlayer.healthRatio == 0) {
            forceTab = false
            return
        }

        if (client.gameState != GameState.LOGGED_IN) {
            forceTab = false
            return
        }

        val searchedWeapon: Weapon = searchForWeapon()
        val hunllef = client.npcs.asSequence().firstOrNull { HunllefIds.contains(it.id) }
        hunllef?.let {
            if (it.isDead || it.healthRatio == 0) {
                // toggle prayers
            }
        }

        if (client.getVarbitValue(9177) != -1) {
            forceTab = false
            return
        }

        if (client.getVarbitValue(9178) != -1) {
            isRanged = true
            forceTab = false
            weapon = Weapon.None
            return
        }

        if (forceTab) {
            client.runScript(915, 3)
            forceTab = false
        }

        if (client.getWidget(5046276) == null) {
            MousePackets.queueClickPacket()
            WidgetPackets.queueWidgetAction(client.getWidget(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB), "Setup")
            forceTab = true
        }


        if (isRanged && !isQuickPrayerActive()) {
            MousePackets.queueClickPacket()
            WidgetPackets.queueWidgetActionPacket(1, 5046276, -1, 13) // quick pray ranged
        } else if (!isRanged && !isQuickPrayerActive()) {
            MousePackets.queueClickPacket()
            WidgetPackets.queueWidgetActionPacket(1, 5046276, -1, 12)
        }


        if (hunllef != null) {
            //
        }
    }

    @Subscribe
    fun onAnimationChanged(event: AnimationChanged) {
        val actor = event.actor ?: return
        if (actor !is NPC) return

        if (!HunllefIds.contains(actor.id)) return

        val animation = actor.animation
        if (animation == 8754 || animation == 8755) {
            isRanged = true
        }
    }

    enum class Weapon { Bow, Halberd, Staff, None; }

    private fun searchForWeapon(): Weapon {
        if (Equipment.search().matchesWildcard("*staff").items.isNotEmpty()) return Weapon.Staff
        if (Equipment.search().matchesWildcard("*bow").items.isNotEmpty()) return Weapon.Bow
        if (Equipment.search().matchesWildcard("*halberd").items.isNotEmpty()) return Weapon.Halberd
        return Weapon.None
    }

    companion object {
        val HunllefIds = setOf(
            NpcID.CORRUPTED_HUNLLEF, NpcID.CORRUPTED_HUNLLEF_9036, NpcID.CORRUPTED_HUNLLEF_9037, NpcID.CORRUPTED_HUNLLEF_9038,
            NpcID.CRYSTALLINE_HUNLLEF, NpcID.CRYSTALLINE_HUNLLEF_9022, NpcID.CRYSTALLINE_HUNLLEF_9024, NpcID.CRYSTALLINE_HUNLLEF_9024
        )
    }
}