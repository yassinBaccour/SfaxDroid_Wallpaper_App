
plugins{
    id("dagger.hilt.android.plugin")
    id(libs.plugins.android.lib.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    kotlin("kapt")
    alias(libs.plugins.ksp)
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
        kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
    }
    namespace = "com.sfaxdroid.list"
}

dependencies {
    implementation( projects.baseAndroid)
    implementation( libs.kotlinx.coroutines.android)
    api (libs.hilt.lib)
    kapt (libs.hilt.android.compiler)
    implementation (libs.compose.ui)
    implementation (libs.compose.material)
    debugImplementation ( libs.compose.ui.tooling)
    implementation( libs.compose.ui.tooling)
    implementation( libs.compose.layout)
    implementation (libs.compose.navigation)
    implementation (libs.compose.nav.animation)
    implementation( libs.compose.coil)
    implementation( libs.androidx.hilt.navigation.compose)
    implementation( projects.domain)

}