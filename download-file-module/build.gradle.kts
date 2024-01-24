
plugins {
    id("com.android.library")
    kotlin("android")
}

android {

    compileSdk = libs.versions.androidCompileSdkVersion.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidMinSdkVersion.get().toInt()
        targetSdk = libs.versions.androidTargetSdkVersion.get().toInt()
    }
    namespace ="com.sfaxdroid.app"

}

dependencies {
    api (libs.thin.download.manager)
}
