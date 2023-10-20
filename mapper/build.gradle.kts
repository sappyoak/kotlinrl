import java.io.File

plugins {
    java
    kotlin("jvm")
    kotlin("plugin.serialization")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":logger"))

    implementation(libs.asm)
    implementation(libs.asm.util)
    implementation(libs.kotlinx.cli)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
}

val mappingOutput = File(project.projectDir, "build")
val mappingInput = File(project.rootProject.projectDir, "devResources/osrs-api-217.jar")

tasks.create("mapApi", JavaExec::class) {
    group = "execution"
    description = "map osrs api classes"
    classpath = sourceSets.main.get().runtimeClasspath + sourceSets.main.get().compileClasspath
    mainClass.set("com.sappyoak.mapper.Main")
    args(
        "-i", "$mappingInput",
        "-dir", "$mappingOutput",
        "-dp"
    )
}