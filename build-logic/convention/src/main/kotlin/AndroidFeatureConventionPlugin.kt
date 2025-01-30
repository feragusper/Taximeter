import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

/**
 * Convention plugin to configure Android feature modules.
 * Applies common plugins and dependencies required for feature modules in the project.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply essential plugins for feature modules
            apply(plugin = "taximeter.android.library")  // Common Android library configurations
            apply(plugin = "taximeter.android.library.compose")  // Jetpack Compose support
            apply(plugin = "taximeter.hilt")  // Dependency injection with Hilt
        }
    }
}

