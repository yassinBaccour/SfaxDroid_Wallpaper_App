
plugins {

    kotlin("android")
    id("com.android.library")
}


android {

    compileSdk =libs.versions.androidCompileSdkVersion.get().toInt()

    defaultConfig {
        minSdk =libs.versions.androidMinSdkVersion.get().toInt()
        targetSdk =libs.versions.androidTargetSdkVersion.get().toInt()
    }
        sourceSets.named("main") {
            jniLibs.srcDir("libs")
        }


    buildFeatures {
        viewBinding = true
    }
    namespace = "com.sfaxdroid.sky"

}

dependencies {
    implementation (libs.rajawali)
}