plugins {
    id("com.android.library")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
}

android {
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.composecompiler.get() }
    namespace = "com.sfaxdroid.list"
}

dependencies {
    api(libs.hilt.lib)
    ksp(libs.hilt.android.compiler)
    implementation(projects.baseAndroid)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.layout)
    implementation(libs.compose.navigation)
    implementation(libs.compose.nav.animation)
    implementation(libs.compose.coil)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(projects.domain)
    implementation(projects.uiDetail)
}
