@file:OptIn(ExperimentalComposeLibrary::class)

import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    kotlin("jvm") version "2.2.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0"
    id("org.jetbrains.compose") version "1.8.2"
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    //id("com.github.johnrengelman.shadow") version "7.1.2"
    id("maven-publish")
}

group = getExtra("mod_group_id")
version = getExtra("mod_version")

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

minecraft {
    mappings(getExtra("mapping_channel"), getExtra("mapping_version"))

    copyIdeResources.set(true)

    runs {
        create("client") {
            workingDirectory(project.file("run/client"))
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")
        }
    }
}

val shadow: Configuration by configurations.creating {
    exclude("org.jetbrains", "annotations")

    isCanBeResolved = true
    isCanBeConsumed = false

    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(KotlinPlatformType.attribute, KotlinPlatformType.jvm)
    }
}

jarJar.enable()

configurations {
    apiElements {
        artifacts.clear()
    }
    runtimeElements {
        setExtendsFrom(emptySet())
        // Publish the jarJar
        artifacts.clear()
        outgoing.artifact(tasks.jarJar)
    }
    minecraftLibrary {
        extendsFrom(shadow)
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    minecraft("net.minecraftforge", "forge", "${getExtra("minecraft_version")}-${getExtra("forge_version")}")
    shadow("org.jetbrains.compose.desktop", "desktop-jvm-windows-x64", "1.8.2")
    shadow("org.jetbrains.compose.material3", "material3", "1.8.2")
    shadow("io.coil-kt.coil3", "coil-compose", "3.3.0")
    shadow("io.coil-kt.coil3", "coil-network-okhttp", "3.3.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${getExtra("mod_group_id")}.${getExtra("mod_id")}"
            artifactId = getExtra("mod_name")
            version = getExtra("mod_version")
            from(components["java"])
        }
    }
}

tasks {
    jarJar.configure {
        archiveBaseName.set(getExtra("mod_id"))
        archiveClassifier.set("")
        //destinationDirectory.set(File("C:\\Users\\rhdwl\\AppData\\Roaming\\MultiMC\\instances\\1.20.1 - 컴포즈 테스트\\.minecraft\\mods"))
        manifest {
            val map = mutableMapOf<String, String>()
            map["Specification-Title"] = getExtra("mod_id")
            map["Specification-Vendor"] = getExtra("mod_authors")
            map["Specification-Version"] = "1"
            map["Implementation-Title"] = project.name
            map["Implementation-Version"] = project.version.toString()
            map["Implementation-Vendor"] = getExtra("mod_authors")
            map["Implementation-Timestamp"] = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
            attributes(map)
        }
        from(provider { shadow.map(::zipTree).toTypedArray() })
    }
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
    assemble {
        dependsOn(jarJar)
    }
}

fun getExtra(key: String): String {
    return extra[key]?.toString() ?: throw NullPointerException("$key is not exists.")
}