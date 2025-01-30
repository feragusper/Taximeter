// Configures the plugin management for the project
pluginManagement {
    repositories {
        gradlePluginPortal() // Official Gradle Plugin repository
        google()             // Google's repository for Android dependencies
    }
}


// Configures dependency resolution for the project
dependencyResolutionManagement {
    repositories {
        google {
            content {
                // Include only the necessary groups from Google's repository to optimize dependency resolution
                includeGroupByRegex("com\\.android.*") // Android-specific dependencies
                includeGroupByRegex("com\\.google.*")  // Google-specific dependencies
                includeGroupByRegex("androidx.*")      // AndroidX libraries
            }
        }
        mavenCentral() // Add Maven Central repository for other dependencies
    }

    // Define a version catalog to manage dependencies centrally via a TOML file
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml")) // Load dependencies from an external TOML file
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
