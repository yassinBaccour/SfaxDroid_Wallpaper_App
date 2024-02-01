
plugins {
    id(libs.plugins.android.lib.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    kotlin("kapt")
    alias(libs.plugins.ksp)
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
    }

    buildFeatures {
        viewBinding = true
    }
    namespace ="com.sfaxdroid.detail"

}

dependencies {
    ksp (libs.glide.compiler)
    implementation (projects.data)
    implementation (projects.baseAndroid)
    implementation (libs.crop)
    implementation (libs.androidx.recyclerview)
    api (libs.hilt.lib)
    kapt( libs.hilt.android.compiler)
}
