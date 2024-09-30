import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

publishing {
    publications.withType<MavenPublication> {
        val javadocStub = tasks.register<Jar>("${name}JavadocJar") {
            dependsOn(project(":docs").getTasksByName("buildDocumentation", false).single())
            archiveClassifier = "javadoc"
            archiveAppendix = this@withType.name
            from("docs/build/dokka/html")
        }
        artifact(javadocStub)
        pom {
            name = "extended-collections"
            description = "An extension of the Kotlin Collections Framework"
            url = "https://github.com/Kotlin/extended-collections"
            licenses {
                license {
                    name = "MIT"
                    url = "https://opensource.org/licenses/MIT"
                }
            }
            issueManagement {
                system = "GitHub"
                url = "https://github.com/Kotlin/extended-collections/issues"
            }
            developers {
                developer {
                    id = "aeckar"
                    name = "Angel Eckardt"
                    organization = "University of South Florida"
                    organizationUrl = "https://www.usf.edu/"
                }
            }
            scm {
                val location = "//github.com/aeckar/extended-collections"
                connection = "scm:git:git:$location.git"
                developerConnection = "scm:git:ssh:$location.git"
                url = "https:$location"
            }
        }
    }
}

signing {
    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
