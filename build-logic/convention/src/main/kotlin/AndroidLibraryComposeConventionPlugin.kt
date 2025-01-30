import com.android.build.gradle.LibraryExtension
import com.feragusper.taximeter.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

/**
 * Gradle plugin to configure Android library modules that use Jetpack Compose.
 *
 * This plugin applies necessary plugins and configuration settings for Compose-based libraries.
 */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply essential plugins required for an Android library using Compose
            apply(plugin = "org.jetbrains.kotlin.plugin.compose") // Kotlin Compose plugin

            // Retrieve the Android library extension to configure Compose-specific settings
            val extension = extensions.getByType<LibraryExtension>()

            // Apply common Compose-related configurations
            configureAndroidCompose(extension)
        }
    }
}