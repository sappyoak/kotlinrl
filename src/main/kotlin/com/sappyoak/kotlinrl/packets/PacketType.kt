package com.sappyoak.kotlinrl.packets

enum class PacketType(val params: List<String>) {
    ResumePauseButton(listOf("var0", "var1")),
    IfButton(listOf("widgetId", "slot", "itemId")),
    OpNpc(listOf("npcIndex", "ctrlDown")),
    OpPlayer(listOf("playerIndex", "ctrlDown")),
    OpObj(listOf("objectId", "worldPointX", "worldPointY", "ctrlDown")),
    OpLoc(listOf("objectId", "worldPointX", "worldPointY", "ctrlDown")),
    MoveGameClick(listOf()),
    EventMouseClick(listOf("mouseInfo", "mouseX", "mouseY")),
    IfButtonT(listOf()),
    OpNpcT(listOf()),
    OpPlayerT(listOf()),
    OpObjT(listOf()),
    OpLocT(listOf());
}