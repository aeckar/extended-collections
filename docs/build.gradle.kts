plugins {
    id("dokkatoo-convention")
}

dependencies {
    dokkatoo(project(":core"))
    dokkatoo(project(":io"))
}

dokkatoo {
    moduleName = "Extended Collections"
    dokkatooPublications.configureEach {
        outputDir = file(".")
        includes.from("../README.md")
    }
}