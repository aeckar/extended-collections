plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.nexus.publish)
    implementation(libs.kotlin)
    implementation(libs.android.library)
    implementation(libs.dokka)
}
