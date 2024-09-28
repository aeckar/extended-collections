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
        includes.from("../README.md")
    }
}

tasks.register<Sync>("buildDocumentation") {  // Workaround for `outputDirs` not working
    dependsOn("dokkatooGenerate")
    copy {
        from("build/dokka/html")
        into(".")
    }
}