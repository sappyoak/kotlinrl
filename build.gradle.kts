import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

plugins {
    java
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0" apply false
}

version = "1.0-SNAPSHOT"

allprojects {
    group = "com.sappyoak"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.runelite.net")
    }
}

val runeLiteVersion = "1.10.14.2"
val repoLiteVersion = getRemoteRuneLiteVersion()
if (repoLiteVersion != runeLiteVersion) {
    println("Warning - Client out of date")
    throw Exception("Out of date\n server: $repoLiteVersion\n local: $runeLiteVersion")
}

project.ext["rlVersion"] = "1.10.14.2"

dependencies {
    implementation("net.runelite:client:$runeLiteVersion")
    testImplementation(kotlin("test"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    manifest.attributes("Main-Class" to "com.sappyoak.kotlinrl.Main")
    val sourcesMain = sourceSets.main.get()
    val contents = configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    } + sourcesMain.output

    from(contents)

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    destinationDirectory.set(file("$rootDir"))
}


tasks.create("runDev", JavaExec::class) {
    group = "execution"
    description = "runs the main method"
    classpath = sourceSets.main.get().runtimeClasspath + sourceSets.main.get().compileClasspath
    mainClass.set("com.sappyoak.kotlinrl.Main")
    jvmArgs("-ea")
}

fun getRemoteRuneLiteVersion(): String? {
    val url = URL("http://repo.runelite.net/net/runelite/client/maven-metadata.xml")
    val connection = url.openConnection()

    var latestName: String? = null
    var inputLine: String

    connection.getInputStream().bufferedReader().useLines { lines ->
        lines.forEach { line ->
            if (line.contains("<release>")) {
                latestName = line.trim().replace("<release>", "").replace("</release>", "")
            }
        }
    }

    return latestName
}

