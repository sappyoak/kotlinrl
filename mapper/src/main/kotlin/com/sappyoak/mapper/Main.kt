package com.sappyoak.mapper

import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.jar.JarFile

import com.sappyoak.logger.getLogger
import com.sappyoak.logger.info
import com.sappyoak.shared.RSApiMapping
import com.sappyoak.shared.RSClass
import com.sappyoak.shared.RSField
import com.sappyoak.shared.RSMethod
import com.sappyoak.shared.RSStructuredMapping

private val FILTERED_PACKAGE_PREFIXES = setOf(
    "com/google",
    "kotlin/",
    "org/",
    "meteor/",
    "eventbus/",
    "javax/",
    "net/runelite"
)
internal val MapperLogger = getLogger("Mapper")

object Main {
    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
        allowSpecialFloatingPointValues = true
    }


    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val config = parseCommandLineArgs(args)
            MapperLogger.info("MapperConfig: $config")
            val apiMapping = mapClasses(readClasses(config.inputJar))

            MapperLogger.info {
                """
                Finished parsing the osrs-api source classes.
                Mapped ${apiMapping.classes.size} classes, ${apiMapping.fields.size} fields, and ${apiMapping.methods.size} methods.
                Preparing to dump output
            """.trimIndent()
            }

            val encoded = json.encodeToString(RSApiMapping.serializer(), apiMapping)
            config.outputFile.writeText(encoded, StandardCharsets.UTF_8)

            if (config.debugPackets) {
                MapperLogger.info("Debugging packets")
                debugPackets(config.outputDir, apiMapping)
            }

        } catch (err: Throwable) {
            err.printStackTrace()
        }
    }

    private fun mapClasses(classMap: Map<String, ClassNode>): RSApiMapping {
        val classes = hashMapOf<String, RSClass>()
        val fields = hashMapOf<String, RSField>()
        val methods = hashMapOf<String, RSMethod>()

        for ((key, value) in classMap) {
            val rsClass = value.getRSClass() ?: continue
            classes[rsClass.name] = rsClass

            value.fields.forEach { field ->
                field.getRSField(rsClass)?.let { fields[field.name] = it }
            }
            value.methods.forEach { method ->
                method.getRSMethod(rsClass)?.let { methods[method.name] = it }
            }
        }

        return RSApiMapping(classes, fields, methods)
    }

    private fun readClasses(input: File): Map<String, ClassNode> {
        val classes = hashMapOf<String, ClassNode>()

        JarFile(input).use { jar ->
            jar.entries().asSequence().forEach {entry ->
                if (!shouldFilterClass(entry.name)) {
                    MapperLogger.info("Reading class '${entry.name}'")
                    jar.getInputStream(entry).readAllBytes().toClassNode().also {
                        classes[it.name] = it
                    }
                }
            }
        }

        return classes
    }

    private fun debugPackets(outputDir: File, mapping: RSApiMapping) {
        val classNamesToFind = setOf("ClientPacket", "ServerPacket", "PacketWriter", "PacketBuffer")

        fun buildStructuredMapping(name: String): RSStructuredMapping? {
            val rsClass = mapping.classes[name] ?: return null
            if (name == "ClientPacket") {
                val entries = mapping.fields.entries.filter { it.value.parent.name == rsClass.name }
                entries.forEach { entry ->
                    MapperLogger.info("ClientPacket: ${entry.key}: ${entry.value.name}:${entry.value.obfName}")
                }
            }

            return RSStructuredMapping(
                rsClass,
                fields = mapping.fields.values.filter { it.parent.name == rsClass.name },
                methods = mapping.methods.values.filter { it.parent.name == rsClass.name }
            )
        }

        val structuredMappings = classNamesToFind.mapNotNull { buildStructuredMapping(it) }
        val encoded = json.encodeToString(ListSerializer(RSStructuredMapping.serializer()), structuredMappings)
        File(outputDir, "packet-debug.json").writeText(encoded)
    }

    private fun shouldFilterClass(name: String): Boolean =
        !name.endsWith(".class") || FILTERED_PACKAGE_PREFIXES.any { name.startsWith(it) }

    private fun ByteArray.toClassNode(): ClassNode {
        val reader = ClassReader(this)
        val node = ClassNode()
        reader.accept(node, ClassReader.SKIP_FRAMES)
        return node
    }
}