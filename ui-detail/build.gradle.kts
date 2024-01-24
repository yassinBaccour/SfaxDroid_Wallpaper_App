
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
    }

    buildFeatures {
        viewBinding = true
    }
    namespace ="com.sfaxdroid.detail"

}

dependencies {
    kapt (libs.glide.compiler)
    implementation (project(":data"))
    implementation (project(":base-android"))
    implementation (libs.crop)
    implementation (libs.androidx.recyclerview)
    api (libs.hilt.lib)
    kapt( libs.hilt.android.compiler)
}
