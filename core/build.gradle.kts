plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("module.publication")
    id("multiplatform.targets")
}

kotlin {
    explicitApi()

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(project(":test-shared"))
        }
    }
}