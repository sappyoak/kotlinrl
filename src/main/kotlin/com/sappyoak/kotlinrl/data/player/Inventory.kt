package com.sappyoak.kotlinrl.data.player

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.Text;
import net.runelite.client.util.WildcardMatcher;

object Inventory {
    val client: Client = RuneLite.getInjector().getInstance(Client::class.java)
    var inventoryItems: List<Widget> = listOf()
    var lastUpdateTick = 0

    fun search(): ItemQuery {
        if (lastUpdateTick < client.tickCount) {
            client.runScript(6009, 9764864, 28, 1, -1)
            inventoryItems = client.getWidget(WidgetInfo.INVENTORY)!!.dynamicChildren.asSequence()
                .filterNotNull()
                .filter { it.itemId != 6512 && it.itemId != -1 }
                .toList()
            lastUpdateTick = client.tickCount
        }

        return ItemQuery(inventoryItems)
    }

    fun getEmptySlots(): Int = 28 - search().items.size
    fun full(): Boolean = getEmptySlots() == 0
    fun getItemAmount(itemId: Int): Int = search().filter { id == itemId }.result().size
    fun getItemAmount(itemName: String): Int = search().filter { name == itemName }.result().size

    @Subscribe
    fun onGameStateChanged(event: GameStateChanged) {
        val state = event.gameState
        if (state == GameState.HOPPING || state == GameState.LOGIN_SCREEN || state == GameState.CONNECTION_LOST) {
            inventoryItems = emptyList()
        }
    }
}

class ItemQuery(passedItems: List<Widget>) {
    private var _items: List<Widget> = passedItems
    val items: List<Widget> get() = _items

    val isEmpty: Boolean get() = items.isEmpty()

    fun filter(predicate: Widget.() -> Boolean): ItemQuery {
        _items = _items.asSequence().filter { it.predicate() }.toList()
        return this
    }

    fun quantityGreaterThan(quantity: Int): ItemQuery = filter {
        itemQuantity > quantity
    }

    fun isInNames(names: List<String>): ItemQuery = filter {
        names.any { it.equals(Text.removeTags(name), ignoreCase = true)}
    }
    fun isInIds(ids: List<Int>): ItemQuery = filter { ids.contains(itemId) }
    fun atIndex(index: Int): ItemQuery = filter { getIndex() == index }

    fun nonPlaceholder(): ItemQuery = quantityGreaterThan(0)
    fun matchesWildcard(input: String): ItemQuery = filter {
        WildcardMatcher.matches(input.lowercase(), Text.removeTags(name.lowercase()))
    }

    fun first(): Widget? = if (items.isEmpty()) null else items[0]
    fun result(): List<Widget> = items

    companion object {
        val client: Client = RuneLite.getInjector().getInstance(Client::class.java)
        val itemManager: ItemManager = RuneLite.getInjector().getInstance(ItemManager::class.java)
    }
}