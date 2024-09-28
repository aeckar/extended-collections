plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.nexus.publish)
    implementation(libs.kotlin.multiplatform)
    implementation(libs.android.library)
    implementation(libs.dokkatoo.html)
}
