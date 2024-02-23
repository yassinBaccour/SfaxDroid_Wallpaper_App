
plugins {
    id("kotlin-android")
    id(libs.plugins.android.lib.get().pluginId)
    alias(libs.plugins.ksp)
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
    implementation("org.rajawali3d:rajawali:1.2.1970@aar")
}