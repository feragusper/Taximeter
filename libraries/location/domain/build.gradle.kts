plugins {
    alias(libs.plugins.taximeter.jvm.library)
    alias(libs.plugins.taximeter.android.test)
}

dependencies {
    implementation(libs.bundles.coroutines)
}