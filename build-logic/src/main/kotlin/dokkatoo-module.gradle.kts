plugins {
    id("dev.adamko.dokkatoo-html")
}

dokkatoo {
    // Ensure that each project has a distinct module path.
    modulePath = rootProject.name + project.path.replace(":", "/")
}