plugins {
    id("org.jetbrains.dokka")
}

tasks.dokkaHtml {
    val dokkaBaseConfiguration = """
        {
            "footerMessage": "© 2024 Angel Eckardt"
        }
    """

    pluginsMapConfiguration = mapOf("org.jetbrains.dokka.base.DokkaBase" to dokkaBaseConfiguration)

    dokkaSourceSets.configureEach {
        includes.from("Module.md")
        skipEmptyPackages = true
        reportUndocumented = true
    }
}