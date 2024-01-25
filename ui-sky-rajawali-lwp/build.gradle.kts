
plugins {

    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.android.lib.get().pluginId)}


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