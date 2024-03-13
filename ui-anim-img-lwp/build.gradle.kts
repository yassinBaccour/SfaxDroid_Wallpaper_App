plugins {
    id("com.android.library")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    buildFeatures {
        compose = true
    }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.composecompiler.get() }
    namespace = "com.sfaxdoird.anim.img"
}

dependencies {
    kapt(libs.hilt.android.compiler)
    api(libs.hilt.lib)
    implementation(libs.colorpicker)
    implementation(projects.baseAndroid)
    implementation(projects.downloadFileModule)
    implementation(libs.fragment.nav.ktx)
    implementation(libs.navigation.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.layout)
    implementation(libs.compose.navigation)
    implementation(libs.compose.nav.animation)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.coil)
    implementation(libs.compose.icon)
    implementation(libs.androidx.hilt.navigation.compose)
}
