plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.feature.onboarding"
    compileSdk { version = release(36) { minorApiLevel = 1 } }

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.koin.android)
    implementation(libs.adapter.delegates)
    implementation(libs.adapter.delegates.layoutcontainer)
    implementation(libs.adapter.delegates.viewbinding)
    implementation(libs.kotlinx.coroutines.android)
}
