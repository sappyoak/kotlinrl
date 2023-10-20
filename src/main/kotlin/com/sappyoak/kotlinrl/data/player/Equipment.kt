package com.sappyoak.kotlinrl.data.player

import net.runelite.api.Client
import net.runelite.api.InventoryID
import net.runelite.api.Item
import net.runelite.api.widgets.Widget
import net.runelite.api.widgets.WidgetInfo
import net.runelite.client.RuneLite
import net.runelite.client.util.Text
import net.runelite.client.util.WildcardMatcher

import java.util.Comparator
import java.util.TreeSet

import com.sappyoak.kotlinrl.data.widgets.EquipmentItemWidget

object Equipment {
    val client: Client = RuneLite.getInjector().getInstance(Client::class.java)
    val equipment = arrayListOf<EquipmentItemWidget>()

    var lastUpdateTick = 0

    val equipmenSlotMapping: Map<Int, Int> by lazy {
        buildMap {
            put(0, 15)
            put(1, 16)
            put(2, 17)
            put(3, 18)
            put(4, 19)
            put(5, 20)
            put(7, 21)
            put(9, 22)
            put(10, 23)
            put(12, 24)
            put(13, 25)
        }
    }

    val mappingToIterableInts: Map<Int, Int> by lazy {
        buildMap {
            put(0, 0)
            put(1, 1)
            put(2, 2)
            put(3, 3)
            put(4, 4)
            put(5, 5)
            put(6, 7)
            put(7, 9)
            put(8, 10)
            put(9, 12)
            put(10, 13)
        }
    }

    fun search(): EquipmentItemQuery {
        if (lastUpdateTick < client.tickCount) {
            var x = 25362447
            for (i in 0..10) {
                client.runScript(545, x + i, mappingToIterableInts[i], 1, 1, 2)
            }

            equipment.clear()
            var index = -1

            if (client.getItemContainer(InventoryID.EQUIPMENT.id) == null) {
                // return new query
                return EquipmentItemQuery(equipment)
            }

            for (item in client.getItemContainer(InventoryID.EQUIPMENT.id)!!.items) {
                index++

                if (item == null) continue
                if (item.id == 6512 || item.id == -1) continue

                val widget = client.getWidget(WidgetInfo.EQUIPMENT.groupId, equipmenSlotMapping[index]!!)
                if (widget?.actions == null) continue

                equipment.add(EquipmentItemWidget(widget.name, item.id, widget.id, index, widget.actions!!))
            }

            lastUpdateTick = client.tickCount
        }

        return EquipmentItemQuery(equipment)
    }
}

class EquipmentItemQuery(passedItems: List<EquipmentItemWidget> = emptyList()) {
    var items: List<EquipmentItemWidget> = passedItems
    val isEmpty: Boolean get() = items.isEmpty()

    fun filter(predicate: EquipmentItemWidget.() -> Boolean): EquipmentItemQuery {
        items = items.asSequence().filter { it.predicate() }.toList()
        return this
    }

    fun nameContains(name: String): EquipmentItemQuery = filter {
        getName().equals(name, ignoreCase = true)
    }

    fun isInIds(ids: List<Int>): EquipmentItemQuery = filter {
        ids.contains(getItemId())
    }

    fun atIndex(index: Int): EquipmentItemQuery = filter {
        getIndex() == index
    }

    fun matchesWildcard(input: String): EquipmentItemQuery = filter {
        WildcardMatcher.matches(input.lowercase(), Text.removeTags(getName().lowercase()))
    }

    fun filterUnique(): EquipmentItemQuery {
        val treeSet = TreeSet<EquipmentItemWidget>(Comparator.comparingInt(Widget::getItemId))
        items.asSequence().forEach { treeSet.add(it) }
        items = treeSet.toList()
        return this
    }

    fun first(): EquipmentItemWidget? = if (items.isEmpty()) null else items[0]
}