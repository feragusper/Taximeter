plugins {
    alias(libs.plugins.taximeter.android.feature)
    alias(libs.plugins.taximeter.android.test)
}

android {
    namespace = "com.feragusper.taximeter.features.taximeter"
}

dependencies {
    // MVI architecture library for state management
    implementation(project(":libraries:mvi"))

    // Design system for consistent UI components and theming
    implementation(project(":libraries:design"))

    // Domain layer of the ride feature (business logic)
    implementation(project(":libraries:ride:domain"))

    // Data layer of the ride feature (repository and data sources)
    implementation(project(":libraries:ride:data"))
}
