plugins {
    id(libs.plugins.android.lib.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sfaxdroid.domain"
}

dependencies {
    implementation(projects.dataAndroid)
    api(projects.data)
    implementation(libs.hilt.lib)
    ksp(libs.hilt.android.compiler)
}
