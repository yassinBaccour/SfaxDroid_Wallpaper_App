
plugins{
    id("kotlin-android")
    id("kotlin-kapt")
    id(libs.plugins.android.lib.get().pluginId)
    id("dagger.hilt.android.plugin")
    id("kotlin-android-extensions")
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
        targetSdk = libs.versions.androidTargetSdkVersion.get().toInt()
    }

    buildFeatures {
        viewBinding = true
    }
    namespace = "com.sfaxdoird.anim.word"

}

dependencies {
    implementation (libs.colorpicker)
    implementation (projects.baseAndroid)
    implementation (projects.downloadFileModule)

    implementation (libs.fragment.nav.ktx)
    implementation (libs.navigation.ktx)

    api( libs.hilt.lib)
    kapt( libs.hilt.android.compiler)
}
