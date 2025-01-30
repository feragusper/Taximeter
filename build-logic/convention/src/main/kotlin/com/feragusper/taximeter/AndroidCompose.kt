package com.feragusper.taximeter

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configures Jetpack Compose-specific settings for Android projects.
 *
 * This function sets up Compose features, dependencies, and compiler configurations
 * to ensure consistency and optimal performance across modules.
 *
 * @param commonExtension Android's [CommonExtension] to configure shared build features and options.
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true // Enable Jetpack Compose
        }

        composeOptions {
            // Set the Kotlin Compose compiler version dynamically from the version catalog
            kotlinCompilerExtensionVersion =
                libs.findVersion("androidxComposeCompiler").get().toString()
        }

        dependencies {
            // Use Compose BOM (Bill of Materials) to manage Compose dependencies' versions
            val bom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))

            // Include Compose UI components and necessary libraries
            "implementation"(libs.findBundle("compose").get())

            // Add Compose UI tooling support for debugging in development builds
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }
}
