plugins {
    id(libs.plugins.android.lib.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id("kotlin-kapt")
}

android {
    namespace = "com.sfaxdroid.domain"
}

dependencies {
    implementation(projects.dataAndroid)
    api(projects.data)
    implementation(libs.hilt.lib)
    kapt(libs.hilt.android.compiler)
}
