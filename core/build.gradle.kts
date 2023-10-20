plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":logger"))
    implementation(project(":shared"))

    implementation("net.runelite:client:${project.rootProject.ext["rlVersion"]}")
}