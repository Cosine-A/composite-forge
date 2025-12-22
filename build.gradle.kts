plugins {
    kotlin("jvm") version "2.2.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0"
    id("org.jetbrains.compose") version "1.8.2"
    id("fabric-loom") version "1.10-SNAPSHOT"
}

version = "0.1.0"
group = "dev.aperso.composite"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    google()
    exclusiveContent {
        forRepository { maven("https://maven.parchmentmc.org") }
        filter { includeGroup("org.parchmentmc.data") }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.1")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.21.1:2024.11.17@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:0.16.14")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.116.3+1.21.1")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.13.4+kotlin.2.2.0")

    val transitiveInclude by configurations.creating {
        exclude("com.mojang")
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }

    api(transitiveInclude(compose.desktop.windows_x64)!!)
    api(transitiveInclude(compose.material3)!!)

    transitiveInclude.resolvedConfiguration.lenientConfiguration.artifacts.forEach {
        include(it.moduleVersion.id.toString())
    }
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}