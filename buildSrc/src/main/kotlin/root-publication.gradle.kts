import java.lang.System.getenv

plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

allprojects {
    group = "io.github.aeckar.collections"
    version = "1.0.0"
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl = uri("https://s01.oss.sonatype.org/service/local/")
            snapshotRepositoryUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            username = getenv("ORG_GRADLE_PROJECT_sonatypeUsername")
            password = getenv("ORG_GRADLE_PROJECT_sonatypePassword")
        }
    }
}
