package com.sappyoak.kotlinrl.data

import net.runelite.api.Client

enum class Region(val regionName: String, val id: Int) {
    Pollnivneach("Pollnivneach", 13358),
    Unknown("Unknown", -1);

    companion object {
        private val idToRegionMap: Map<Int, Region> by lazy {
            val map = mutableMapOf<Int, Region>()
            for (region in values()) {
                map[region.id] = region
            }
            map
        }

        fun from(region: Int): Region = idToRegionMap[region] ?: Unknown
    }
}

fun Client.isInRegion(region: Region): Boolean =
    mapRegions.asSequence().any { it == region.id }

fun Client.isInRegion(region: Int): Boolean = isInRegion(Region.from(region))