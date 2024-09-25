plugins {
    kotlin("jvm")
}

kotlin {
    sourceSets.main {
        dependencies {
            implementation(project(":core"))
        }
    }
}