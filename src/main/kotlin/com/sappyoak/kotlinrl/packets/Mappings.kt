package com.sappyoak.kotlinrl.packets

data class ObfuscatedMethod(
    val name: String,
    val garbage: String
)

data class ObfuscatedMappings(
    val packetBufferNodeParentClass: String = "fv",
    val clientPacketClass: String = "ln",
    val bufferClass: String = "ul",
    val packetWriterClass: String = "ez",
    val packetBufferNodeClass: String = "lx",
    val getPacketBufferNodeMethod: ObfuscatedMethod = ObfuscatedMethod(
        name = "al",
        garbage = "2045853930"
    ),
    val addNodeMethod: ObfuscatedMethod = ObfuscatedMethod(name = "ak", garbage = "22"),
    val packetWriterField: String = "ic",
    val isaacCipherField: String = "ao",
    val packetBufferField: String = "ak",
    val bufferArrayField: String = "ab",
    val bufferOffsetField: String = "am",
    val mouseLastPressedTimeClass: String = "bo",
    val mouseLastPressedTimeField: String = "aw",
    val clientMouseLastPressedMs: String = "el"
)


object PacketMethodMappings {
    val OpLoc2 = listOf("dy", "dp", "dy", "dt")
    val OpLoc4 = listOf("dt", "dd", "dd", "do")

    val OpObj3 = listOf("dt", "dd", "dd", "dl")
    val OpObj4 = listOf("dy", "bu", "bu", "do")

    val OpPlayer1 = listOf("dl", "dd")
    val OpPlayer5 = listOf("dp", "dt")
    val OpPlayer7 = listOf("bc", "bu")

    val OpNpc3 = listOf("dl", "dd")
    val OpNpc5 = listOf("bc", "dt")

    val IfButton = listOf("bh", "bu", "bu")

    val EventMouseClick = listOf("bu", "bu", "bu")
    val RememberPauseButton = listOf("bu", "ez")

}