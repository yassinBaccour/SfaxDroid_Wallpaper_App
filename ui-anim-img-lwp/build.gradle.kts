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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.compose.compiler.toString()
    }
    namespace = "com.sfaxdoird.anim.img"

}

dependencies {
    implementation( libs.colorpicker)
    implementation( project(":base-android"))
    implementation( project(":download-file-module"))
    implementation( libs.fragment.nav.ktx)
    implementation( libs.navigation.ktx)
    api( libs.hilt.lib)
    kapt( libs.hilt.android.compiler)
    implementation( libs.kotlinx.coroutines.android)
    implementation( libs.compose.ui)
    implementation( libs.compose.material)
    implementation( libs.compose.layout)
    implementation( libs.compose.navigation)
    implementation( libs.compose.animation)
    implementation( libs.compose.ui.tooling)
    implementation( libs.coil)
    implementation( libs.icon)
    debugImplementation(  libs.compose.ui.tooling)
    implementation( libs.androidx.hilt.navigation.compose)

}
