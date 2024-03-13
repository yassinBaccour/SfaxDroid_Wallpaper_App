plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.sfaxdroid.detail"
}

dependencies {
    kapt(libs.glide.compiler)
    kapt(libs.hilt.android.compiler)
    api(libs.hilt.lib)
    implementation(projects.data)
    implementation(projects.baseAndroid)
    implementation("com.soundcloud.android:android-crop:1.0.1@aar")
    implementation(libs.androidx.recyclerview)
}
