plugins {
    id("root-publication")
    alias(libs.plugins.dokka)
}

tasks.dokkaHtmlMultiModule {
    delete("docs/") // Replace folder
    outputDirectory = file("docs/")
    moduleName = "Extended Collections"
}