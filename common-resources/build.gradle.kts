plugins {
    id(libs.plugins.android.lib.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id("kotlin-kapt")
}

android {
    namespace="com.commun.resources"
}