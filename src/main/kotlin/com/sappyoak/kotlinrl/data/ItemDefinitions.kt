package com.sappyoak.kotlinrl.data

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Inject

import net.runelite.api.Client
import net.runelite.api.ItemComposition
import net.runelite.client.RuneLite
import net.runelite.client.game.ItemManager

import java.util.concurrent.TimeUnit

object ItemDefinitions {
    val itemManager: ItemManager = RuneLite.getInjector().getInstance(ItemManager::class.java)

    val defs: LoadingCache<Int, ItemComposition> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(20, TimeUnit.MINUTES)
        .build(object : CacheLoader<Int, ItemComposition>() {
            override fun load(itemId: Int): ItemComposition =
                itemManager.getItemComposition(itemId)
        })
}