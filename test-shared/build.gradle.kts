plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("multiplatform.targets")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.test)
            implementation(project(":core"))
        }
    }
}