import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val currentVersion = "0.1.0"
val myGroup = "com.mituuz.cobol-forge"

intellijPlatform {
    buildSearchableOptions = false

    pluginConfiguration {
        version = currentVersion
        group = myGroup

        changeNotes = """
            Initial release
        """.trimIndent()

        description = """
            A small COBOL language plugin for IntelliJ.
            Provides COPY previews by hovering over inlay hints.
        """.trimIndent()

        ideaVersion {
            sinceBuild = "242"
            untilBuild = provider { null }
        }
    }
}

plugins {
    id("java")
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.intellijPlatform)
}

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2.0.1")
        testFramework(TestFrameworkType.Platform)
    }

    testImplementation(libs.junit5Api)
    testImplementation(libs.junit5Engine)
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java", "src/main/gen")
        }
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}