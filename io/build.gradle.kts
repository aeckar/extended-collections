import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("module-publication")
    id("multiplatform-targets")
    id("dokka-convention")
    alias(libs.plugins.dokka)
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.io)
            api(project(":core"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(project(":test-logic"))
        }
    }
}