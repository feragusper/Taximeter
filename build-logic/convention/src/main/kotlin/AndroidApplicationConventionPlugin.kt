import com.android.build.api.dsl.ApplicationExtension
import com.feragusper.taximeter.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin to configure Android application projects.
 * Applies necessary plugins and configures common settings across all Android app modules.
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply core plugins required for Android applications
            apply(plugin = "com.android.application")  // Android application plugin
            apply(plugin = "org.jetbrains.kotlin.android") // Kotlin support for Android
            apply(plugin = "taximeter.hilt")  // Dependency injection with Hilt

            // Configure the Android application-specific settings
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)  // Apply common Kotlin Android configurations

                // Set default target SDK for the app
                defaultConfig.targetSdk = 35
            }
        }
    }
}
