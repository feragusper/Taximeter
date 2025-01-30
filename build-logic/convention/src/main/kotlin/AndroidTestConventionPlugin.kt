import com.android.build.gradle.LibraryExtension
import com.feragusper.taximeter.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin to configure Android testing with standardized settings and dependencies.
 */
class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Configure unit test tasks to use JUnit 5 platform
            tasks.withType(org.gradle.api.tasks.testing.Test::class.java).configureEach {
                useJUnitPlatform()
            }

            pluginManager.withPlugin("com.android.base") {
                // Configure Android library settings related to testing
                extensions.configure<LibraryExtension> {
                    @Suppress("UnstableApiUsage") // Suppress warnings for experimental API usage
                    testOptions {
                        animationsDisabled = true // Disable animations to improve test reliability

                        unitTests {
                            isIncludeAndroidResources =
                                true // Include Android resources for Robolectric tests
                        }
                    }
                }

                dependencies {
                    // Additional debugging tools for Compose UI tests
                    add(
                        "debugImplementation",
                        libs.findLibrary("androidx-compose-ui-test-manifest").get()
                    )
                }
            }

            // Add necessary test dependencies
            dependencies {
                // Common test libraries bundle
                add("testImplementation", libs.findBundle("test").get())

                // JUnit Jupiter for unit tests
                add("testRuntimeOnly", libs.findLibrary("junit-jupiter-engine").get())

                // JUnit Vintage for backward compatibility
                add("testRuntimeOnly", libs.findLibrary("junit-vintage-engine").get())

            }
        }
    }
}
