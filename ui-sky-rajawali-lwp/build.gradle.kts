plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    sourceSets.named("main") { jniLibs.srcDir("libs") }
    namespace = "com.sfaxdroid.sky"
}

dependencies { implementation("org.rajawali3d:rajawali:1.2.1970@aar") }
