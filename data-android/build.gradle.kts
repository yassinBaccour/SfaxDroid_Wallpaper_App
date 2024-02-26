
plugins{
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    alias(libs.plugins.ksp)
}
kapt {
    correctErrorTypes = true
    useBuildCache = true
}

android {

    compileSdk = libs.versions.androidCompileSdkVersion.get().toInt()

            defaultConfig {
                minSdk = libs.versions.androidMinSdkVersion.get().toInt()
                targetSdk  = libs.versions.androidTargetSdkVersion.get().toInt()
            }
    namespace = "com.sfaxdroid.data"

}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.hilt.lib)
    kapt(libs.hilt.android.compiler)
    implementation(projects.baseAndroid)
    api(projects.data)
    implementation(libs.room.ktx)
    api(libs.room.runtime)
    ksp(libs.room.compiler)
}