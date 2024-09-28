plugins {
    id("module-publication")
    id("multiplatform-targets")
    id("dokkatoo-convention")
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

dokkatoo {
    moduleName = "Extended Collections Core"
    modulePath = "core"
}