package com.sappyoak.kotlinrl.packets

import javax.inject.Inject
import net.runelite.api.Client
import kotlin.math.abs
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.math.BigInteger

object PacketReflection {
    val mappings: ObfuscatedMappings = ObfuscatedMappings()

    lateinit var classWithPacketBufferNodeGetter: Class<*>
    lateinit var packetBufferNodeGetter: Method

    lateinit var ClientPacket: Class<*>
    var IsaacCipherClass: Class<*>? = null
    var PacketBufferNode: Class<*>? = null

    lateinit var PacketWriter: Field
    lateinit var isaac: Any

    lateinit var mouseHandlerLastPressedTime: Field
    lateinit var clientMouseLastPressedTimeMs: Field

    @Inject
    lateinit var clientInstance: Client

    var client: Client? = null

    fun loadPackets(): Boolean {
        try {
            client = clientInstance
            classWithPacketBufferNodeGetter = clientInstance::class.java.classLoader.loadClass(mappings.packetBufferNodeParentClass)
            ClientPacket = clientInstance::class.java.classLoader.loadClass(mappings.clientPacketClass)
            PacketWriter = clientInstance::class.java.getDeclaredField(mappings.packetWriterField)
            PacketBufferNode = clientInstance::class.java.classLoader.loadClass(mappings.packetBufferNodeClass)

            PacketWriter.isAccessible = true

            val isaac2 = PacketWriter.get(null).javaClass.getDeclaredField(mappings.isaacCipherField)
            isaac2.isAccessible = true
            isaac = isaac2.get(PacketWriter.get(null))
            isaac2.isAccessible = false
            PacketWriter.isAccessible = false

            IsaacCipherClass = isaac::class.java
            packetBufferNodeGetter = classWithPacketBufferNodeGetter.declaredMethods.asSequence()
                .filter { method -> method.returnType == PacketBufferNode }
                .toList()
                .first()

            mouseHandlerLastPressedTime = clientInstance::class.java.classLoader.loadClass(mappings.mouseLastPressedTimeClass)
                .getDeclaredField(mappings.mouseLastPressedTimeField)
            clientMouseLastPressedTimeMs = clientInstance::class.java.getDeclaredField(mappings.clientMouseLastPressedMs)
        } catch (err: Throwable) {
            err.printStackTrace()
            return false
        }
        return true
    }

    fun writeObject(obfname: String, buffer: Any, input: Any) {
        val bufferMethod = BufferMethods.javaClass.getDeclaredMethod(obfname, Any::class.java, Int::class.java)
        bufferMethod.invoke(null, buffer, input)
    }

    fun sendPacket(packet: Packet, vararg args: Any) {
        val packetBufferNode = getPacketBufferNode()
        val buffer = packetBufferNode.javaClass.getDeclaredField(mappings.packetBufferField).get(packetBufferNode)
        packetBufferNodeGetter.isAccessible = false

        for ((index, fieldPair) in packet.fields.withIndex()) {
            writeObject(fieldPair.method, buffer, args[index])
        }

        PacketWriter.isAccessible = true
        try {
            addNode(PacketWriter.get(null), packetBufferNode)
        } catch (err: Throwable) { err.printStackTrace() }

        PacketWriter.isAccessible = false
    }

    fun addNode(packetWriter: Any, packetBufferNode: Any) {
        try {
            var addNodeMethod: Method? = null
            val (name, garbageStr) = mappings.addNodeMethod
            val garbageValue: Long = garbageStr.toLong()

            if (garbageValue < 256L) {
                addNodeMethod =
                    packetWriter.javaClass.getDeclaredMethod(name, packetBufferNode.javaClass, Byte.javaClass)
                addNodeMethod.isAccessible = true
                addNodeMethod.invoke(packetWriter, packetBufferNode, garbageStr.toByte())
            } else if (garbageValue < 32768L) {
                addNodeMethod =
                    packetWriter.javaClass.getDeclaredMethod(name, packetBufferNode.javaClass, Short.javaClass)
                addNodeMethod.isAccessible = true
                addNodeMethod.invoke(packetWriter, packetBufferNode, garbageStr.toShort())
            } else if (garbageValue < Int.MAX_VALUE) {
                addNodeMethod =
                    packetWriter.javaClass.getDeclaredMethod(name, packetBufferNode.javaClass, Int.javaClass)
                addNodeMethod.isAccessible = true
                addNodeMethod.invoke(packetWriter, packetBufferNode, garbageStr.toInt())
            }


            if (addNodeMethod != null) {
                addNodeMethod.isAccessible = false
            }
        } catch (err: Throwable) {
            err.printStackTrace()
        }
    }

    fun fetchPacketField(name: String): Field = ClientPacket.getDeclaredField(name)

    private fun getPacketBufferNode(): Any {
        packetBufferNodeGetter.isAccessible = true
        val garbageStr: String = mappings.getPacketBufferNodeMethod.garbage
        val garbageValue = abs(garbageStr.toLong())

        val packetField = fetchPacketField("").get(ClientPacket)

        return when {
            garbageValue < 256L -> packetBufferNodeGetter.invoke(null, packetField, isaac, garbageStr.toByte())
            garbageValue < 32768L -> packetBufferNodeGetter.invoke(null, packetField, isaac, garbageStr.toShort())
            garbageValue < Int.MAX_VALUE -> packetBufferNodeGetter.invoke(null, packetField, isaac, garbageStr.toInt())
            else -> error("Invalid value for garbage $garbageValue")
        }
    }

    private fun modInverse(num: BigInteger): BigInteger {
        val shift = BigInteger.ONE.shiftLeft(32)
        return num.modInverse(shift)
    }

    private fun modInverse(num: Long): Int = modInverse(BigInteger.valueOf(num)).intValueExact()
    private fun modInverse(num: Int): Int = modInverse(num.toLong())

}