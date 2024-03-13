plugins {
    id("com.android.library")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.sfaxdoird.anim.word"
}

dependencies {
    kapt(libs.hilt.android.compiler)
    implementation(libs.colorpicker)
    implementation(projects.baseAndroid)
    implementation(projects.downloadFileModule)
    implementation(libs.fragment.nav.ktx)
    implementation(libs.navigation.ktx)
    api(libs.hilt.lib)
}
