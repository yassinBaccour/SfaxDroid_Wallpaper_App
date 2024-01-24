
plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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

    buildFeatures {
        viewBinding = true
    }
    namespace= "com.sfaxdroid.timer"

}

dependencies {
    implementation (project(":base-android"))
    implementation( libs.kotlinx.coroutines.android)
    api (libs.hilt.lib)
    kapt (libs.hilt.android.compiler)
    implementation( project(":domain"))
}
