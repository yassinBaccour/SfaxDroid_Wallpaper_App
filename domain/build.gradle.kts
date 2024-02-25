plugins {
    id(libs.plugins.android.lib.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id("kotlin-kapt")
    alias(libs.plugins.ksp)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

android {
    compileSdk = libs.versions.androidCompileSdkVersion.get().toInt()
    defaultConfig {
        minSdk = libs.versions.androidMinSdkVersion.get().toInt()
        targetSdk = libs.versions.androidTargetSdkVersion.get().toInt()
    }
    namespace = "com.sfaxdroid.domain"
}

dependencies {
    implementation(projects.dataAndroid)
    api(projects.data)
    implementation(libs.hilt.lib)
    kapt(libs.hilt.android.compiler)
}
