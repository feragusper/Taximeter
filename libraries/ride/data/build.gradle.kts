plugins {
    alias(libs.plugins.taximeter.android.library)
    alias(libs.plugins.taximeter.hilt)
    alias(libs.plugins.taximeter.android.test)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.feragusper.taximeter.libraries.ride.data"
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.coroutines)
    implementation(project(":libraries:ride:domain"))
    implementation(project(":libraries:location:domain"))
    implementation(project(":libraries:location:data"))
}