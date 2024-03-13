plugins {
    id("com.android.library")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.sfaxdroid.timer"
}

dependencies {
    kapt(libs.hilt.android.compiler)
    api(libs.hilt.lib)
    implementation(projects.baseAndroid)
    implementation(libs.kotlinx.coroutines.android)
    implementation(projects.domain)
}