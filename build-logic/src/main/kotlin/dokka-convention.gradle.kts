import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("org.jetbrains.dokka")
}

tasks.withType<DokkaTask>().configureEach {
    pluginsMapConfiguration =
        mapOf("org.jetbrains.dokka.base.DokkaBase" to "{ \"footerMessage\": \"© 2024 Angel Eckardt\" }")

    dokkaSourceSets.configureEach {
        includes.from("../gradle/Module.md")
        samples.from("../samples/src/main/kotlin/io/github/aeckar/collections/samples/Samples.kt")
        skipEmptyPackages = true
        reportUndocumented = true
    }
}