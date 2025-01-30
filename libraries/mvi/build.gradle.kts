plugins {
    alias(libs.plugins.taximeter.android.library)
    alias(libs.plugins.taximeter.android.test)
}

android {
    namespace = "com.feragusper.taximeter.libraries.mvi"
}

dependencies {
    implementation(libs.bundles.coroutines)
}