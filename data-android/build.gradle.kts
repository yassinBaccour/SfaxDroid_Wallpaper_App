plugins{
    id("com.android.library")
    id("kotlin-android")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sfaxdroid.data"
}

dependencies {
    ksp(libs.hilt.android.compiler)
    ksp(libs.room.compiler)
    api(projects.data)
    api(libs.room.runtime)
    implementation(libs.kotlin.stdlib)
    implementation(libs.hilt.lib)
    implementation(projects.baseAndroid)
    implementation(libs.room.ktx)
}