
plugins{
    id("dagger.hilt.android.plugin")
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
        multiDexEnabled = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.compose.compiler.toString()
    }
    namespace = "com.sfaxdroid.list"
}

dependencies {

    implementation( project(":base-android"))
    implementation( libs.kotlinx.coroutines.android)

    api (libs.hilt.lib)
    kapt (libs.hilt.android.compiler)
    
    implementation (libs.compose.ui)
    implementation (libs.compose.material)
    debugImplementation ( libs.compose.ui.tooling)
    implementation( libs.compose.ui.tooling)
    implementation( libs.compose.layout)
    implementation (libs.compose.navigation)
    implementation (libs.compose.animation)
    implementation( libs.coil)
    implementation( libs.androidx.hilt.navigation.compose)

    implementation( project(":domain"))

}