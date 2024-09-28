plugins {
    id("dev.adamko.dokkatoo-html")
}

dokkatoo {
    // Ensure that each project has a distinct module path.
    modulePath = rootProject.name + project.path.replace(":", "/")

    dokkatooSourceSets.configureEach {
        includes.from("../Module.md")
        sourceLink {
            localDirectory = rootDir
            remoteUrl("https://github.com/aeckar/${rootProject.name}/tree/master")
            remoteLineSuffix = "#L"
        }
        reportUndocumented = true
    }
    pluginsConfiguration.html {
        footerMessage = "© Angel Eckardt"
        separateInheritedMembers = true
    }
}