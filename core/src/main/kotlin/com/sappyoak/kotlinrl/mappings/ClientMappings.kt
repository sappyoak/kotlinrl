package com.sappyoak.kotlinrl.mappings


import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.runelite.api.Client
import net.runelite.client.RuneLite

import com.sappyoak.shared.RSApiMapping
import com.sappyoak.shared.RSClass
import com.sappyoak.shared.RSField
import com.sappyoak.shared.RSMethod

object ClientMappings {
    val client: Client = RuneLite.getInjector().getInstance(Client::class.java)
    val json = Json {}

    private lateinit var mappings: RSApiMapping
    private val fileName: String get() = "api-${client.revision}-mappings.json"

    private lateinit var packetWriterRS: RSClass
    private lateinit var getPacketBufferNodeRS: RSMethod
    private lateinit var addNodeRS: RSMethod
    private lateinit var packetBufferNodesRS: RSField
    private lateinit var packetBufferRS: RSField
    private lateinit var bufferSizeRS: RSField
    private lateinit var serverPacketRS: RSField
    private lateinit var serverPacketLengthRS: RSField
    private lateinit var packetWriterFieldRS: RSField


    fun getRSClass(name: String): RSClass? = mappings.classes[name]
    fun getRSField(parent: String, name: String): RSField? =
        mappings.fields.values.firstOrNull { it.parent.name == parent && it.name == name }


    @OptIn(ExperimentalSerializationApi::class)
    fun load() {
        val stream = ClientMappings.javaClass.classLoader.getResourceAsStream(fileName)
            ?: error("Could not load mappings file from resources")

        try {
            mappings = Json.decodeFromStream(stream)
        } catch (err: Throwable) {
            err.printStackTrace()
        }
    }
}