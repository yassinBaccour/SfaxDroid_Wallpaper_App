
plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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
    implementation (project(":data-android"))
    api (project(":data"))
    implementation (libs.hilt.lib)
    kapt( libs.hilt.compiler)

}
