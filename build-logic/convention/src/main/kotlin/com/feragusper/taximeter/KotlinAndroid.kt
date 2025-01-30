package com.feragusper.taximeter

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Configures Kotlin and Android-specific settings for a given project.
 *
 * @param commonExtension The Android common extension to configure.
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = 35  // Set the compile SDK version

        defaultConfig {
            minSdk = 21  // Minimum SDK supported
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            isCoreLibraryDesugaringEnabled = true
        }
    }

    // Apply common Kotlin configurations
    configureKotlin<KotlinAndroidProjectExtension>()

    // Add desugaring library for backward compatibility
    dependencies {
        "coreLibraryDesugaring"(libs.findLibrary("android-desugar-jdk-libs").get())
    }
}

/**
 * Configures Kotlin JVM-specific settings for a non-Android project.
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        // Set Java compatibility version to 17 for JVM projects
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    // Apply common Kotlin configurations for JVM projects
    configureKotlin<KotlinJvmProjectExtension>()
}

/**
 * Configures general Kotlin compiler options for both Android and JVM projects.
 */
private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    // Configure compiler options with default JVM target and experimental features
    val warningsAsErrors: String? by project  // Allow override via gradle.properties

    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> TODO("Unsupported project extension: $this ${T::class}")
    }.apply {
        jvmTarget = JvmTarget.JVM_21  // Target JVM 21 compatibility
        allWarningsAsErrors =
            warningsAsErrors.toBoolean()  // Treat warnings as errors if set in properties

        freeCompilerArgs.addAll(
            listOf(
                // Enable experimental coroutines APIs, including Flow
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                // Handle Kotlin data class copy method visibility changes (for future compatibility)
                "-Xconsistent-data-class-copy-visibility"
            )
        )
    }
}
