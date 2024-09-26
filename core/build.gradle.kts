import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("module-publication")
    id("multiplatform-targets")
    id("dokka-convention")
    alias(libs.plugins.dokka)
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets.configureEach {
        samples.from(project(":core:samples"))
    }
}

kotlin {
    explicitApi()

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(project(":test-logic"))
        }
    }
}