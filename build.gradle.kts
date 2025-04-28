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
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    id("org.jetbrains.intellij.platform") version "2.0.1"
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

        instrumentationTools()

        testFramework(TestFrameworkType.Platform)
    }

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.4")
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java", "src/main/gen")
        }
        kotlin {
            srcDir("src/main/kotlin")
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