package com.sappyoak.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class GarbageType {
    Byte,
    Short,
    Int,
    Unknown;

    companion object {
        fun from(str: String): GarbageType = when (str) {
            "B" -> Byte
            "S" -> Short
            "I" -> Int
            else -> Unknown
        }
        fun from(char: Char): GarbageType = from(char.toString())
    }
}


@Serializable
data class RSApiMapping(
    val classes: Map<String, RSClass>,
    val fields: Map<String, RSField>,
    val methods: Map<String, RSMethod>
)

@Serializable
data class RSClass(val name: String, val obfName: String)

@Serializable
data class RSField(
    val parent: RSClass,
    val name: String,
    val obfName: String,
    val type: String
)

@Serializable
data class RSMethod(
    val parent: RSClass,
    val name: String,
    val obfName: String,
    val descriptor: String,
    val garbageValue: String,
    val garbageType: GarbageType
)

@Serializable
data class RSStructuredMapping(
    @SerialName("class") val rsClass: RSClass,
    val fields: List<RSField> = emptyList(),
    val methods: List<RSMethod> = emptyList()
)