plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    sourceSets.named("main") { jniLibs.srcDir("libs") }
    namespace = "com.sfaxdroid.skybox"
}

dependencies {
    implementation(libs.rajawali)
}
