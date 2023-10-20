package com.sappyoak.kotlinrl.extensions

import net.runelite.api.HeadIcon
import net.runelite.api.NPC

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

var animationField: String? = null

fun NPC?.animation(): Int {
    if (this == null) return -1
    if (animationField == null) {
        for (field: Field? in javaClass.superclass.declaredFields) {
            if (field == null) continue

            field.isAccessible = true

            if (field.type != Int::class.java) continue
            if (Modifier.isFinal(field.modifiers)) continue
            if (Modifier.isStatic(field.modifiers)) continue

            val value = field.getInt(this)
            field.setInt(this, 4795789)

            if (animation == 1375718357 * 479578) {
                animationField = field.name
                field.setInt(this, value)
                field.isAccessible = false
                break
            }

            field.setInt(this, value)
            field.isAccessible = false
        }

        if (animationField == null) return -1
    }

    val anim = javaClass.superclass.getDeclaredField(animationField)
    anim.isAccessible = true
    val animInt = anim.getInt(this) * 1375718357
    anim.isAccessible = false
    return animInt
}

fun NPC.headIcon(): HeadIcon? {
    var getHeadIconArrayMethod: Method
    for (method in composition.javaClass.declaredMethods) {
        if (method.returnType == ShortArray::class.java && method.parameterTypes.isEmpty()) {
            getHeadIconArrayMethod = method

            getHeadIconArrayMethod.isAccessible = true
            val headIconArray = getHeadIconArrayMethod.invoke(composition) as ShortArray
            if (headIconArray.isEmpty()) continue

            return HeadIcon.values()[headIconArray[0].toInt()]
        }
    }
    return null
}