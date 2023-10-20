package com.sappyoak.mapper

import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode


import com.sappyoak.shared.GarbageType
import com.sappyoak.shared.RSClass
import com.sappyoak.shared.RSField
import com.sappyoak.shared.RSMethod

const val Export = "Lnet/runelite/mapping/Export;"
const val Import = "Lnet/runelite/mapping/Import";
const val ObfuscatedClientPacket = "Lnet/runelite/mapping/ObfuscatedClientPacket;"
const val ObfuscatedName = "Lnet/runelite/mapping/ObfuscatedName;"
const val ObfuscatedSignature = "Lnet/runelite/mapping/ObfuscatedSignature;";

fun AnnotationNode.extractFirstValue(): String? =
    if (values.size >= 2) values[1].toString() else null

fun AnnotationNode.extractSecondValue(): String? {
    if (values.size == 3) return ""
    return if (values.size >= 4) values[3].toString() else null
}

fun ClassNode.getRSClass(): RSClass? {
    if (visibleAnnotations == null) return null

    for (annotation in visibleAnnotations) {
        if (annotation.desc == ObfuscatedName) {
            return annotation.extractFirstValue()?.let {
                RSClass(name, it)
            }
        }
    }

    return null
}

// name, obf, type
fun FieldNode.getRSField(parent: RSClass): RSField? {
    if (visibleAnnotations == null) return null

    val type = desc
    var readableName = name
    var obfName: String? = null

    for (annotation in visibleAnnotations) {
        when (annotation.desc) {
            ObfuscatedName -> obfName = annotation.extractFirstValue()
            Export -> annotation.extractFirstValue()?.let { readableName = it }
            else -> {}
        }
    }

    return obfName?.let { RSField(parent, readableName, obfName, type)}
}

fun MethodNode.getRSMethod(parent: RSClass): RSMethod? {
    if (visibleAnnotations == null) return null

    var obfuscatedSignature: String = ""
    var obfuscatedName: String = ""
    var readableName: String = name
    var garbageValue: String = ""

    for (annotation in visibleAnnotations) {
        when (annotation.desc) {
            ObfuscatedName -> annotation.extractFirstValue()?.let {
                obfuscatedName = it
            }
            Export -> annotation.extractFirstValue()?.let {
                readableName = it
            }
            ObfuscatedSignature -> {
                annotation.extractFirstValue()?.let { obfuscatedSignature = it }
                annotation.extractSecondValue()?.let { garbageValue = it }
            }
            else -> {}
        }
    }

    // Don't even bother mappping anything without a signature. We won't use it
    if (obfuscatedSignature == "") return null

    val endsArgsIndex = obfuscatedSignature.indexOf(")")
    val garbageType = GarbageType.from(obfuscatedSignature[endsArgsIndex - 1])


    return RSMethod(
        parent,
        readableName,
        obfuscatedName,
        obfuscatedSignature,
        garbageValue,
        garbageType
    )
}