plugins {
    id("root-publication")
    id("dokkatoo-convention")
}

tasks["build"].dependsOn(project(":docs").getTasksByName("buildDocumentation", false))