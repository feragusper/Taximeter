plugins {
    alias(libs.plugins.taximeter.android.library)
    alias(libs.plugins.taximeter.hilt)
    alias(libs.plugins.taximeter.android.test)
}

android {
    namespace = "com.feragusper.taximeter.libraries.location.data"
}

dependencies {
    implementation(project(":libraries:location:domain"))
    implementation(libs.bundles.coroutines)
}