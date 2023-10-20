package com.sappyoak.kotlinrl.packets

import java.lang.reflect.Field

object BufferMethods {
    const val bufferArrayField: String = "ab"
    const val bufferOffsetField: String = "am"

    fun dt(bufferInstance: Any, writtenValue: Int): Unit = performOperation(bufferInstance, writtenValue) { arr, offset ->
        var currentOffset = offset

        var next = currentOffset.calculateOffsetPairing()
        currentOffset = next.first
        arr[next.second] = writtenValue.toByte()

        next = currentOffset.calculateOffsetPairing()
        currentOffset = next.first
        arr[next.second] = (writtenValue shr 8).toByte()

        currentOffset
    }

    fun dd(bufferInstance: Any, writtenValue: Int): Unit = performOperation(bufferInstance, writtenValue) { arr, offset ->
        var currentOffset = offset

        var next = currentOffset.calculateOffsetPairing()
        currentOffset = next.first
        arr[next.second] = (writtenValue shr 8).toByte()

        next = currentOffset.calculateOffsetPairing()
        currentOffset = next.first
        arr[next.second] = (128 + writtenValue).toByte()

        currentOffset
    }

    fun bc(bufferInstance: Any, writtenValue: Int): Unit = performOperation(bufferInstance, writtenValue) { arr, offset ->
        var currentOffset = offset

        val next = currentOffset.calculateOffsetPairing()
        currentOffset = next.first
        arr[next.second] = writtenValue.toByte()
        currentOffset
    }

    fun bu(bufferInstance: Any, writtenValue: Int): Unit = performOperation(bufferInstance, writtenValue) { arr, offset ->
        var currentOffset = offset

        var next = currentOffset.calculateOffsetPairing()
        currentOffset = next.first
        arr[next.second] = (writtenValue shr 8).toByte()

        next = currentOffset.calculateOffsetPairing()
        currentOffset = next.first
        arr[next.second] = writtenValue.toByte()

        currentOffset
    }

    fun dy(bufferInstance: Any, writtenValue: Int): Unit =
        performOperation(bufferInstance, writtenValue) { arr, offset ->
            var currentOffset = offset

            var next = currentOffset.calculateOffsetPairing().also { currentOffset = it.first }
            arr[next.second] = (writtenValue + 128).toByte()

            next = currentOffset.calculateOffsetPairing().also { currentOffset = it.first }
            arr[next.second] = (writtenValue shr 8).toByte()

            currentOffset
        }

    fun ez(bufferInstance: Any, writtenValue: Int): Unit =
        performOperation(bufferInstance, writtenValue) { arr, offset ->
            var currentOffset = offset

            var next = currentOffset.calculateOffsetPairing().also { currentOffset = it.first }
            arr[next.second] = (writtenValue shr 8).toByte()

            next = currentOffset.calculateOffsetPairing().also { currentOffset = it.first }
            arr[next.second] = writtenValue.toByte()

            next = currentOffset.calculateOffsetPairing().also { currentOffset = it.first }
            arr[next.second] = (writtenValue shr 24).toByte()

            next = currentOffset.calculateOffsetPairing().also { currentOffset = it.first }
            arr[next.second] = (writtenValue shr 16).toByte()

            currentOffset
        }

    fun bh(bufferInstance: Any, writtenValue: Int): Unit =
        performOperation(bufferInstance, writtenValue) { arr, offset ->
            var currentOffset = offset

            var next = currentOffset.calculateOffsetPairing().also { currentOffset = it.first }
            arr[next.second] = (writtenValue shr 24).toByte()

            next = currentOffset.calculateOffsetPairing().also { currentOffset = it.first }
            arr[next.second] = (writtenValue shr 16).toByte()

            next = currentOffset.calculateOffsetPairing().also { currentOffset = it.first }
            arr[next.second] = (writtenValue shr 8).toByte()

            next = currentOffset.calculateOffsetPairing().also { currentOffset = it.first }
            arr[next.second] = writtenValue.toByte()

            currentOffset
        }

    fun performOperation(bufferInstance: Any, writtenValue: Int, block: (ByteArray, Int) -> Int) {
        val (arrayField, offsetField) = getBufferField(bufferInstance)
        arrayField.isAccessible = true
        offsetField.isAccessible = true

        val array = arrayField.get(bufferInstance) as ByteArray
        var offset = offsetField.getInt(bufferInstance)

        offset = block(array, offset)

        offsetField.setInt(bufferInstance, offset)
        arrayField.set(bufferInstance, array)

        arrayField.isAccessible = false
        offsetField.isAccessible = false
    }

    private fun getBufferField(bufferInstance: Any): Pair<Field, Field> {
        val arrayField = bufferInstance::class.java.getField(bufferArrayField)
        val offsetField = bufferInstance.javaClass.getField(bufferOffsetField)
        return arrayField to offsetField
    }

    private fun Int.calculateOffsetPairing(): Pair<Int, Int> {
        val newValue = this + -1456241929
        val indexValue = newValue * -2114593081 - 1
        return newValue to indexValue
    }
}