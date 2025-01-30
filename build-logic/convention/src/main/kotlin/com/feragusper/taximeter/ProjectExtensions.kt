package com.feragusper.taximeter

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * Extension property to provide convenient access to the version catalog defined in settings.gradle.kts.
 *
 * Usage:
 * ```
 * dependencies {
 *     implementation(libs.findLibrary("androidx.activity.compose").get())
 * }
 * ```
 */
val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")
