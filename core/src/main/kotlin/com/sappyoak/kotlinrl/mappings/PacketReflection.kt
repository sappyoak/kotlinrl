package com.sappyoak.kotlinrl.mappings

import java.lang.reflect.Field
import java.lang.reflect.Method

import net.runelite.api.Client

object PacketReflection {
    lateinit var ClientPacket: Class<*>
    lateinit var PacketWriter: Field
    lateinit var IsaacCipher: Class<*>
    lateinit var PacketBufferNode: Class<*>

    lateinit var getPacketBufferNode: Method
    lateinit var clientInstance: Client

    lateinit var isaac: Any

    fun loadPackets(): Boolean {
        ClientPacket = loadClass(ClientMappings.getRSClass("ClientPacket")!!.obfName)
        PacketWriter = clientInstance.javaClass.getDeclaredField(
            ClientMappings.getRSField("Client", "packetWriter")!!.obfName
        )
        PacketBufferNode = loadClass(ClientMappings.getRSClass("PacketBufferNode")!!.obfName)

        val packetBuffer = PacketWriter.get(null).javaClass.getDeclaredField(
            ClientMappings.getRSField("PacketWriter", "packetBuffer")!!.obfName
        )
        packetBuffer.isAccessible = true
        isaac = packetBuffer.get(PacketWriter.get(null))
        packetBuffer.isAccessible = false
        PacketWriter.isAccessible = false

        IsaacCipher = isaac.javaClass

    }

    private fun loadClass(name: String): Class<*> =
        clientInstance.javaClass.classLoader.loadClass(name)
}