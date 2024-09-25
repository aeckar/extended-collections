plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("module.publication")
    id("multiplatform.targets")
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
            implementation(project(":test-shared"))
        }
    }
}