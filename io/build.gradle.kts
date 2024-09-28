plugins {
    id("module-publication")
    id("multiplatform-targets")
    id("dokkatoo-convention")
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

dokkatoo {
    moduleName = "Extended Collections I/O"
    modulePath = "io"
}