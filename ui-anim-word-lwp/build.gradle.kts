plugins {
    id("com.android.library")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.sfaxdoird.anim.word"
}

dependencies {
    ksp(libs.hilt.android.compiler)
    implementation(libs.colorpicker)
    implementation(projects.baseAndroid)
    implementation(projects.downloadFileModule)
    implementation(libs.fragment.nav.ktx)
    implementation(libs.navigation.ktx)
    api(libs.hilt.lib)
}
