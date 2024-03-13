plugins {
    id(libs.plugins.android.lib.get().pluginId)
    id("kotlin-android")
    id("kotlin-kapt")
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

android {
    namespace = "com.sfaxdroid.base"
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.composecompiler.get() }
}

dependencies {
    api(projects.base)
    api(projects.commonResources)
    kapt(libs.glide.compiler)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.appcompat)
    api(libs.glide)
    api(libs.coordinatorlayout)
    api(libs.google.material)
    api(libs.lifecycle.livedata.ktx)
    api(libs.lifecycle)
    api(libs.lifecycle.viewmodel.ktx)
    api(libs.fragment.ktx)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.layout)
    implementation(libs.compose.navigation)
    implementation(libs.compose.nav.animation)
    implementation(libs.compose.coil)
}