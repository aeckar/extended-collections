plugins {
    kotlin("jvm")
    alias(libs.plugins.dokka)
}

kotlin {
    sourceSets.main {
        dependencies {
            implementation(project(":core"))
        }
    }
}