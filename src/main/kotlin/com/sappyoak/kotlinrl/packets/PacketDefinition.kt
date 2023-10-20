package com.sappyoak.kotlinrl.packets

data class PacketFieldPair(val field: String, val method: String)

data class Packet(
    val name: String,
    val fields: List<PacketFieldPair>,
    val type: PacketType
) {
    companion object {
        val OpObj3 = buildPacket("", PacketType.OpObjT, PacketMethodMappings.OpObj3)
        val OpObj4 = buildPacket("", PacketType.OpObjT, PacketMethodMappings.OpObj4)

        val IfButton1 = buildIfButton("bq")
        val IfButton2 = buildIfButton("dz")
        val IfButton3 = buildIfButton("by")
        val IfButton4 = buildIfButton("cf")
        val IfButton5 = buildIfButton("ae")
        val IfButton6 = buildIfButton("df")
        val IfButton7 = buildIfButton("cz")
        val IfButton8 = buildIfButton("bw")
        val IfButton9 = buildIfButton("bk")
        val IfButton10 = buildIfButton("dg")

        val EventMouseClick = buildPacket("cd", PacketType.EventMouseClick, PacketMethodMappings.EventMouseClick)
        val ResumePauseButton = buildPacket("ba", PacketType.ResumePauseButton, PacketMethodMappings.RememberPauseButton)
    }
}

fun buildIfButton(name: String): Packet = buildPacket(name, PacketType.IfButton, PacketMethodMappings.IfButton)

fun buildPacket(name: String, type: PacketType, methods: List<String>): Packet {
    val fields = type.buildPacketFieldPairs(methods)
    return Packet(name, fields, type)
}
fun PacketType.buildPacketFieldPairs(methods: List<String>): List<PacketFieldPair> =
    params.mapIndexed { index, param -> PacketFieldPair(param, methods[index]) }

