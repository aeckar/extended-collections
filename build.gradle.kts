plugins {
    id("root-publication")
    id("dokkatoo-convention")
}

tasks["build"].dependsOn(project(":docs").getTasksByName("dokkatooGenerate", false))

dokkatoo {
    dokkatooPublications.configureEach {
        includes.from("Module.md")
        includes.from("README.md")
    }
}