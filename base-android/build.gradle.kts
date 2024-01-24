
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

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.compose.compiler.toString()
    }
    namespace = "com.sfaxdroid.base"

}

dependencies {
    api(project(":base"))
    api(project(":common-resources"))
    kapt(libs.glide.compiler)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.appcompat)
    api(libs.glide)
    api(libs.coordinatorlayout)
    api(libs.google.material)
    api(libs.lifecycle.livedata.ktx)
    api(libs.lifecycle.viewmodel.ktx)
    api(libs.lifecycle)
    api(libs.fragment.ktx)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.layout)
    implementation(libs.compose.navigation)
    implementation(libs.compose.animation)
    implementation(libs.coil)
}
