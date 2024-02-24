plugins {
    id("com.android.library")
    id(libs.plugins.kotlin.android.get().pluginId)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.composeVersion.get() }
    namespace = "com.sfaxdoird.anim.img"
}

dependencies {
    implementation(libs.colorpicker)
    implementation(projects.baseAndroid)
    implementation(projects.downloadFileModule)
    implementation(libs.fragment.nav.ktx)
    implementation(libs.navigation.ktx)
    api(libs.hilt.lib)
    kapt(libs.hilt.android.compiler)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.layout)
    implementation(libs.compose.navigation)
    implementation(libs.compose.nav.animation)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.coil)
    implementation(libs.compose.icon)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.androidx.hilt.navigation.compose)
}
