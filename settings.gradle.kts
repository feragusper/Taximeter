// =======================================
// Plugin Management Configuration
// =======================================
pluginManagement {
    // Include custom build logic from the 'build-logic' module
    includeBuild("build-logic")

    // Define repositories for resolving plugins
    repositories {
        google()             // Google's Maven repository (Android dependencies)
        mavenCentral()        // Central Maven repository for Java/Kotlin libraries
        gradlePluginPortal()  // Gradle's official plugin repository
    }
}

// =======================================
// Dependency Resolution Management
// =======================================
dependencyResolutionManagement {
    // Prevents project-level repositories from being used,
    // enforcing dependency resolution through central repositories only.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Define global repositories for dependency resolution
    repositories {
        google()             // Google's Maven repository
        mavenCentral()        // Central Maven repository
    }
}

// =======================================
// Project Structure Configuration
// =======================================
rootProject.name = "Taximeter"

// ---------------------------------------
// Include application and library modules
// ---------------------------------------
include(":app")

// Libraries
include(":libraries:design")
include(":libraries:mvi")
include(":libraries:location:domain")
include(":libraries:location:data")

// Ride domain and data layers
include(":libraries:ride:domain")
include(":libraries:ride:data")

// Feature modules
include(":features:taximeter")