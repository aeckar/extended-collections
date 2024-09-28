plugins {
    id("dev.adamko.dokkatoo-html")
}

dokkatoo {
    // Ensure that each project has a distinct module path.
    modulePath = rootProject.name + project.path.replace(":", "/")

    dokkatooSourceSets.configureEach {
        includes.from("../Module.md")
        sourceLink {
            val module = project.name
            val sourceSet = name
            val srcDir = "src/$sourceSet/kotlin"
            localDirectory = projectDir.resolve(srcDir)
            remoteUrl("https://github.com/aeckar/${rootProject.name}/$module/$srcDir")
        }
    }
}