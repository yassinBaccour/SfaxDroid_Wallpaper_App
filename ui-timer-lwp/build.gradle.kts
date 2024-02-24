plugins {
    id(libs.plugins.android.lib.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id("dagger.hilt.android.plugin")
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
        multiDexEnabled = true
    }
    buildFeatures { viewBinding = true }
    namespace = "com.sfaxdroid.timer"
}

dependencies {
    implementation(projects.baseAndroid)
    implementation(libs.kotlinx.coroutines.android)
    api(libs.hilt.lib)
    kapt(libs.hilt.android.compiler)
    implementation(projects.domain)
}
