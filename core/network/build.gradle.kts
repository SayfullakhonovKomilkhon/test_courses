plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.core.network"
    compileSdk { version = release(36) { minorApiLevel = 1 } }

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.moshi.kotlin)
    implementation(libs.koin.android)
}
