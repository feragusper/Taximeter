import com.feragusper.taximeter.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

/**
 * JVM Library Convention Plugin.
 *
 * This plugin centralizes the configuration for JVM-based modules, ensuring consistency
 * across different projects. It applies the necessary Kotlin JVM plugin and dependencies.
 *
 * Features:
 * - Applies the Kotlin JVM plugin.
 * - Configures standard Kotlin JVM settings.
 * - Adds necessary test dependencies.
 */
class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply Kotlin JVM plugin
            apply(plugin = "org.jetbrains.kotlin.jvm")

            // Configure Kotlin JVM-specific options (from shared function)
            configureKotlinJvm()
        }
    }
}
