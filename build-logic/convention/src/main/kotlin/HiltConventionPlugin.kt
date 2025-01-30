import com.feragusper.taximeter.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

/**
 * Hilt Convention Plugin for centralized dependency management and automatic configuration
 * for projects using Hilt.
 *
 * - Applies the Hilt Android Gradle Plugin for Android modules.
 * - Adds necessary dependencies based on project type (Android or JVM).
 */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply Kotlin Symbol Processing (KSP) for Hilt annotation processing
            apply(plugin = "com.google.devtools.ksp")

            // Add Hilt compiler dependency for KSP processing
            dependencies {
                "ksp"(libs.findLibrary("hilt-compiler").get())
            }

            // Configure dependencies for JVM modules (applies if 'org.jetbrains.kotlin.jvm' is applied)
            pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
                dependencies {
                    "implementation"(libs.findLibrary("hilt-core").get())
                }
            }

            // Configure dependencies for Android modules (applies if 'com.android.base' is applied)
            pluginManager.withPlugin("com.android.base") {
                apply(plugin = "dagger.hilt.android.plugin")
                dependencies {
                    "implementation"(libs.findLibrary("hilt-android").get())
                }
            }
        }
    }
}
