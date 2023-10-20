package com.sappyoak.kotlinrl.data.widgets

import net.runelite.api.FontTypeFace
import net.runelite.api.Point
import net.runelite.api.widgets.Widget

import java.awt.*

class EquipmentItemWidget(
    val name: String,
    val itemId: Int,
    val packetId: Int,
    val index: Int,
    val actions: Array<out String>
) : Widget {
    val type: Int = 0
    val contentType: Int = 0
    val clickMask: Int = 0

    override fun getId(): Int = packetId
    override fun getType(): Int = type
    override fun setType(type: Int) {}
    override fun getContentType(): Int = contentType
    override fun setContentType(contentType: Int): Widget? = null
    override fun getClickMask(): Int = clickMask
    override fun setClickMask(mask: Int): Widget? = null
    override fun getParent(): Widget? = null
    override fun getParentId(): Int = 0
    override fun getChild(index: Int): Widget? = null
    override fun getChildren(): Array<out Widget>? = null
    override fun setChildren(children: Array<out Widget>) {}

    override fun getDynamicChildren(): Array<out Widget>? = emptyArray()
    override fun getStaticChildren(): Array<out Widget>? = emptyArray()
    override fun getNestedChildren(): Array<out Widget>? = emptyArray()
    override fun getRelativeX(): Int = 0

    @Deprecated("Deprecated in Java")
    override fun setRelativeX(x: Int) {}

    override fun setForcedPosition(x: Int, y: Int) {}
    override fun getText(): String? = null
    override fun setText(text: String): Widget? = null

    override fun getTextColor(): Int = 0
    override fun setTextColor(textColor: Int): Widget? = null

    override fun getOpacity(): Int = 0
    override fun setOpacity(opacity: Int): Widget? = null

    override fun getName(): String = name
    override fun setName(name: String): Widget? = null

    override fun getModelId(): Int = 0
    override fun setModelId(id: Int): Widget? = null

    override fun getModelType(): Int = 0
    override fun setModelType(type: Int): Widget? = null

    override fun getAnimationId(): Int = 0
    override fun setAnimationId(animationId: Int): Widget? = null

    override fun getRotationX(): Int = 0
    override fun setRotationX(modelX: Int): Widget? = null

    override fun getRotationY(): Int = 0
    override fun setRotationY(modelY: Int): Widget? = null

    override fun getRotationZ(): Int = 0
    override fun setRotationZ(modelZ: Int): Widget? = null

    override fun getModelZoom(): Int = 0
    override fun setModelZoom(modelZoom: Int): Widget? = null

    override fun getSpriteId(): Int = 0
    override fun setSpriteId(spriteId: Int): Widget? = null

    override fun getSpriteTiling(): Boolean = false
    override fun setSpriteTiling(tiling: Boolean): Widget? = null

    override fun isHidden(): Boolean = false
    override fun isSelfHidden(): Boolean = false
    override fun setHidden(hidden: Boolean): Widget? = null

    override fun getIndex(): Int = -1
    override fun getCanvasLocation(): Point? = null
    override fun getWidth(): Int = 0
    override fun setWidth(width: Int) {}

    override fun getHeight(): Int = 0
    override fun setHeight(height: Int) {}

    override fun getBounds(): Rectangle? = null
    override fun getItemId(): Int = -1

    override fun setItemId(itemId: Int): Widget? = null

    override fun getItemQuantity(): Int = 1
    override fun setItemQuantity(quantity: Int): Widget? = null

    override fun contains(point: Point): Boolean = false

    override fun getScrollX(): Int = 0
    override fun setScrollX(scrollX: Int): Widget? = null

    override fun getScrollY(): Int = 0
    override fun setScrollY(scrollY: Int): Widget? = null

    override fun getScrollWidth(): Int = 0
    override fun setScrollWidth(width: Int): Widget? = null

    override fun getScrollHeight(): Int = 0
    override fun setScrollHeight(height: Int): Widget? = null

    override fun getOriginalX(): Int = 0
    override fun setOriginalX(originalX: Int): Widget? = null

    override fun getOriginalY(): Int = 0
    override fun setOriginalY(originalY: Int): Widget? = null

    override fun setPos(x: Int, y: Int): Widget? = null
    override fun setPos(x: Int, y: Int, xMode: Int, yMode: Int): Widget? = null

    override fun getOriginalHeight(): Int = 0
    override fun setOriginalHeight(originalHeight: Int): Widget? = null

    override fun getOriginalWidth(): Int = 0
    override fun setOriginalWidth(originalWidth: Int): Widget? = null

    override fun setSize(width: Int, height: Int): Widget? = null
    override fun setSize(width: Int, height: Int, widthMode: Int, heightMode: Int): Widget? = null

    override fun getActions(): Array<out String>? = emptyArray()

    override fun createChild(index: Int, type: Int): Widget? = null
    override fun createChild(type: Int): Widget? = null

    override fun deleteAllChildren() {}
    override fun setAction(index: Int, action: String) {}
    override fun setOnOpListener(vararg args: Any?) {}
    override fun setOnDialogAbortListener(vararg args: Any?) {}
    override fun setOnKeyListener(vararg args: Any?) {}
    override fun setOnMouseOverListener(vararg args: Any?) {}
    override fun setOnMouseRepeatListener(vararg args: Any?) {}
    override fun setOnMouseLeaveListener(vararg args: Any?) {}
    override fun setOnTimerListener(vararg args: Any?) {}
    override fun setOnTargetEnterListener(vararg args: Any?) {}
    override fun setOnTargetLeaveListener(vararg args: Any?) {}

    override fun hasListener(): Boolean = false
    override fun setHasListener(hasListener: Boolean): Widget? = null

    override fun isIf3(): Boolean = false
    override fun revalidate() {}
    override fun revalidateScroll() {}
    override fun getOnOpListener(): Array<Any> = emptyArray()
    override fun getOnKeyListener(): Array<Any> = emptyArray()
    override fun getOnLoadListener(): Array<Any> = emptyArray()
    override fun getOnInvTransmitListener(): Array<Any> = emptyArray()

    override fun getFontId(): Int = 0
    override fun setFontId(id: Int): Widget? = null

    override fun getBorderType(): Int = 0
    override fun setBorderType(thickness: Int) {}

    override fun getTextShadowed(): Boolean = false
    override fun setTextShadowed(shadowed: Boolean): Widget? = null



    companion object {
        const val EQUIPMENT_INVENTORY_ITEMS_CONTAINER = 25362449
    }
}