plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {

    defaultConfig {
        applicationId = "com"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    flavorDimensions += listOf("app")

    productFlavors {
        create("wallpaperAppOne") {
            applicationIdSuffix = ".sami.rippel.allah"
            versionCode = 101
            versionName = "10.0.0"
            dimension = "app"
        }
        create("wallpaperAppTwo") {
            applicationIdSuffix = ".liliagame.scarewhotouchme"
            versionCode = 160034
            versionName = "6.6.3"
            dimension = "app"
        }
    }

    namespace = "com.sfaxdroid.wallpaper"

}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.ui.wallpapers)
    implementation(projects.ui.details)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.hilt.library)
    implementation(libs.okhttp)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

android.applicationVariants.all {
    buildConfigField("String", "SFAXDROID_BASE_URL", "\"http://vps-eeb86c7c.vps.ovh.net\"")
    buildConfigField("String", "PARTNERS_BASE_URL", "\"https://pixabay.com\"")
    buildConfigField("String", "PARTNERS_API_KEY", "\"19985524-f627984e6e929e47e060fd2ff\"")
    buildConfigField("String", "JSON_V", "\"1\"")
}