plugins {
    id("dokkatoo-module")
}

dependencies {
    dokkatoo("io.github.aeckar.collections:core")
    dokkatoo("io.github.aeckar.collections:io")
}

dokkatoo {
    moduleName = "Extended Collections"
}