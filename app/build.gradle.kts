plugins {
    alias(libs.plugins.taximeter.android.application)
}

android {
    namespace = "com.feragusper.taximeter"

    defaultConfig {
        applicationId = "com.feragusper.taximeter"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources.excludes.addAll(
            listOf("/META-INF/{AL2.0,LGPL2.1}")
        )
    }
}

dependencies {
    implementation(project(":libraries:design"))
    implementation(project(":features:taximeter"))
}