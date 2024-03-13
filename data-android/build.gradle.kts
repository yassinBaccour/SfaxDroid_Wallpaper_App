plugins{
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.sfaxdroid.data"
}

dependencies {
    kapt(libs.hilt.android.compiler)
    kapt(libs.room.compiler)
    api(projects.data)
    api(libs.room.runtime)
    implementation(libs.kotlin.stdlib)
    implementation(libs.hilt.lib)
    implementation(projects.baseAndroid)
    implementation(libs.room.ktx)
}