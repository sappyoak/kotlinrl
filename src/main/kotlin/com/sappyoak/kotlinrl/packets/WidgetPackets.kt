package com.sappyoak.kotlinrl.packets

import net.runelite.api.widgets.Widget
import net.runelite.client.util.Text

object WidgetPackets {
    fun queueWidgetActionPacket(field: Int, widgetId: Int, itemId: Int, childId: Int) {
        PacketReflection.sendPacket(mapFieldToPacket(field), widgetId, itemId, childId)
    }

    fun queueWidgetAction(widget: Widget?, vararg actions: String?) {
        if (widget == null) return

        val lowercased = actions.toList().map { it?.lowercase() }

        var num = -1

        for (action in lowercased) {
            for (originalAction in actions) {
                if (action != null && Text.removeTags(action).equals(originalAction, ignoreCase = true)) {
                    num = actions.indexOf(action.lowercase()) + 1
                }
            }
        }

        if (num < 1 || num > 10) return
        queueWidgetActionPacket(num, widget.id, widget.itemId, widget.index)
    }

    fun queueWidgetOnWidget(src: Widget, dest: Widget) {
        queueWidgetOnWidget(src.id, src.index, src.itemId, dest.itemId, dest.index, dest.itemId)
    }

    fun queueWidgetOnWidget(
        sourceId: Int,
        sourceSlot: Int,
        sourceItemId: Int,
        destId: Int,
        destSlot: Int,
        destItemId: Int
    ) {
        PacketReflection.sendPacket(
            Packet.IfButton7,
            sourceId,
            sourceSlot,
            sourceItemId,
            destId,
            destSlot,
            destItemId
        )
    }

    fun queueResumePause(widgetId: Int, childId: Int) {
        PacketReflection.sendPacket(Packet.ResumePauseButton, widgetId, childId)
    }

    private fun mapFieldToPacket(field: Int): Packet = when (field) {
        1 -> Packet.IfButton1
        2 -> Packet.IfButton2
        3 -> Packet.IfButton3
        4 -> Packet.IfButton4
        5 -> Packet.IfButton5
        6 -> Packet.IfButton6
        7 -> Packet.IfButton7
        8 -> Packet.IfButton8
        9 -> Packet.IfButton9
        10 -> Packet.IfButton10
        else -> error("'$field' is not a valid field value for a Widget packet")
    }
}