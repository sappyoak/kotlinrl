package com.sappyoak.kotlinrl.packets

import net.runelite.api.Client
import net.runelite.client.RuneLite

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import java.awt.event.KeyEvent
import java.math.BigInteger
import java.util.Random
import java.util.concurrent.Executors

import java.awt.event.InputEvent.BUTTON1_DOWN_MASK

object MousePackets {
    val client: Client = RuneLite.getInjector().getInstance(Client::class.java)

    val mouseHandlerGarbage: String = "-1264438020177004003"
    val clientMouseHandlerGarbage: String = "-1781940677952610751"
    val clientMouseSetterGarbage: String = "-2382823752758256419"

    private val random = Random()
    private var randomDelay = randomDelay()


    fun modInverse(value: BigInteger, bits: Int): BigInteger {
        return try {
            val shift = BigInteger.ONE.shiftLeft(bits)
            value.modInverse(shift)
        } catch (err: Throwable) {
            value
        }
    }

    fun modInverse(value: Long): Long = modInverse(BigInteger.valueOf(value), 64).longValueExact()

    fun queueClickPacket(x: Int = 0, y: Int = 0) {
        PacketReflection.mouseHandlerLastPressedTime.isAccessible = true
        PacketReflection.clientMouseLastPressedTimeMs.isAccessible = true

        val currentTime = System.currentTimeMillis()
        PacketReflection.mouseHandlerLastPressedTime.setLong(null, currentTime * modInverse(mouseHandlerGarbage.toLong()))
        val clientMs = clientMouseHandlerGarbage.toLong() * (PacketReflection.clientMouseLastPressedTimeMs.get(null) as Long)
        val mouseMs = System.currentTimeMillis()
        var deltaMs = mouseMs - clientMs
        if (deltaMs < 0) {
            deltaMs = 0L
        }
        if (deltaMs > 32767) {
            deltaMs = 32767L
        }

        PacketReflection.clientMouseLastPressedTimeMs.set(client, clientMouseSetterGarbage.toLong() * (PacketReflection.mouseHandlerLastPressedTime.get(null) as Long))
        val mouseInfo = (deltaMs shl 1).toInt()

        PacketReflection.sendPacket(Packet.EventMouseClick, mouseInfo, x, y)
        PacketReflection.mouseHandlerLastPressedTime.isAccessible = false
        PacketReflection.clientMouseLastPressedTimeMs.isAccessible = false

        if (checkIdleLogout()) {
            randomDelay = randomDelay()
            Executors.newSingleThreadExecutor().submit(MousePackets::pressKey)
        }

    }

    private fun checkIdleLogout(): Boolean {
        var idleClientTicks = client.keyboardIdleTicks
        if (client.mouseIdleTicks < idleClientTicks) {
            idleClientTicks = client.mouseIdleTicks
        }
        return idleClientTicks >= randomDelay
    }

    private fun randomDelay(): Long = clamp(round(random.nextGaussian() * 8000)).toLong()
    private fun clamp(num: Double): Double = max(1.0, min(13000.0, num))
    private fun pressKey() {
        val keyPress = KeyEvent(client.canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), BUTTON1_DOWN_MASK, KeyEvent.VK_BACK_SPACE)
        client.canvas.dispatchEvent(keyPress)

        val keyRelease = KeyEvent(client.canvas, KeyEvent.KEY_FIRST, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE)
        client.canvas.dispatchEvent(keyRelease)

        val keyTyped = KeyEvent(client.canvas, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE)
        client.canvas.dispatchEvent(keyTyped)
    }
}