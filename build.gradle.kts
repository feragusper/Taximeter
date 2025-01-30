// Apply necessary Gradle plugins for the project with their aliases and disable immediate application.
plugins {
    // Android application plugin for managing the app module configuration.
    alias(libs.plugins.android.application) apply false

    // Android library plugin for handling library modules.
    alias(libs.plugins.android.library) apply false

    // Android test plugin for setting up testing configurations.
    alias(libs.plugins.android.test) apply false

    // Compose plugin for enabling Jetpack Compose support.
    alias(libs.plugins.compose) apply false

    // Kotlin JVM plugin for Kotlin projects targeting the JVM.
    alias(libs.plugins.kotlin.jvm) apply false

    // Kotlin serialization plugin for handling JSON serialization/deserialization.
    alias(libs.plugins.kotlin.serialization) apply false

    // Hilt plugin for dependency injection.
    alias(libs.plugins.hilt) apply false

    // KSP (Kotlin Symbol Processing) plugin for annotation processing.
    alias(libs.plugins.ksp) apply false
}
