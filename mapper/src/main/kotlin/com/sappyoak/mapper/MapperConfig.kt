package com.sappyoak.mapper

import kotlinx.cli.*
import java.io.File
import java.io.InputStream

import com.sappyoak.shared.Files

private val currentRevision: String by lazy {
    Files.loadCurrentRevision()
}

data class MapperConfig(
    private val inputJarName: String = "osrs-api-217.jar",
    private var outputFileName: String = "api-mappings.json",
    private val outputDirectory: String,
    val debugPackets: Boolean = false
) {
    val inputJar: File get() = File(inputJarName)
    val outputDir: File get() = File(outputDirectory)
    val outputFile: File get() = File(outputDir, outputFileName)
}

fun parseCommandLineArgs(args: Array<String>): MapperConfig {
    val parser = ArgParser("mapper")

    var inputJarName by parser.option(
        ArgType.String,
        shortName = "i",
        description = "The annotated, deobfuscated osrs source jar"
    ).default("osrs-api-$currentRevision.jar")

    val outputFileName by parser.option(
        ArgType.String,
        shortName = "o",
        description = "The output filename for the generated mappings file"
    ).default("api-$currentRevision-mappings.json")

    val outputDirectory by parser.option(
        ArgType.String,
        shortName = "dir",
        description = "The directory to output the mappings file to"
    ).default(Files.Root.toString())

    val debugPackets by parser.option(
        ArgType.Boolean,
        shortName = "dp",
        description = "Output the information related to packet classes into smaller, more individual files for debugging"
    ).default(false)

    parser.parse(args)
    return MapperConfig(inputJarName, outputFileName, outputDirectory, debugPackets)
}