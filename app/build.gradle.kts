import java.nio.file.Files
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.ksp)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

android {
    namespace = "com.yassin.wallpaper"
    compileSdk = libs.versions.androidCompileSdkVersion.get().toInt()
    defaultConfig {
        applicationId = "com"
        minSdk = libs.versions.androidMinSdkVersion.get().toInt()
        targetSdk = libs.versions.androidTargetSdkVersion.get().toInt()
        multiDexEnabled = true
        buildConfigField("String", "APP_KEY", "\"https://www.google.com\"")
        buildConfigField("String", "JSON_VERSION", "\"v3\"")
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["dagger.hilt.disableModulesHaveInstallInCheck"] = "true"
            }
        }
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions("app")

    productFlavors {
        create("accountOne") {
            applicationIdSuffix = ".sami.rippel.allah"
            dimension = "app"
            versionCode = 92
            versionName = "9.2"
            buildConfigField("String", "APP_INTERSTITIAL_KEY", "\"ca-app-pub-6263632629106733/3118481881\"")
            manifestPlaceholders["hostName"] = "ca-app-pub-6263632629106733~1726613607"
        }

        create("accountTwo") {
            applicationIdSuffix = ".liliagame.scarewhotouchme"
            dimension = "app"
            resConfigs("ar", "xxhdpi")
            versionCode = 30
            versionName = "3.0"
            buildConfigField("String", "APP_INTERSTITIAL_KEY", "ADS_INTERSTITIAL_KEY_LILIAGAME")
            manifestPlaceholders["hostName"] = "ca-app-pub-6263632629106733~4067989099"
        }
    }

    packagingOptions {
        exclude("META-INF/ASL2.0")
        exclude("META-INF/LICENSE")
        exclude("META-INF/NOTICE")
        exclude("LICENSE.txt")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/MANIFEST.MF")
    }

    sourceSets {
        getByName("main"){
            jniLibs.srcDir("libs")
        }
    }

    compileOptions {
        sourceCompatibility= JavaVersion.VERSION_1_8
        targetCompatibility =JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
    }

    lintOptions {
        isAbortOnError = false
        isCheckDependencies = true
        isCheckReleaseBuilds = false
        disable("StringFormatMatches","Instantiatable")
        isIgnoreTestSources = true
    }
}

dependencies {
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.fragment.nav.ktx)
    implementation(libs.navigation.ktx)
    implementation(libs.hilt.lib)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.google.material)
    implementation(libs.google.services.ads)
    implementation(libs.google.core)
    implementation(libs.google.core.ktx)
    implementation(libs.google.messaging.platform)
    implementation(libs.glide)
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.crashlytics.lib)
    implementation(libs.timber)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.layout)
    implementation(libs.compose.navigation)
    implementation(libs.compose.nav.animation)
    implementation(libs.compose.coil)
    implementation(libs.compose.icon)
    implementation(projects.downloadFileModule)
    implementation(projects.baseAndroid)
    implementation(projects.domain)
    implementation(projects.uiDetail)
    implementation(projects.uiSkyRajawaliLwp)
    implementation(projects.uiAnimImgLwp)
    implementation(projects.uiAnimWordLwp)
    implementation(projects.uiTimerLwp)
    implementation(projects.uiList)
    implementation(projects.dataAndroid)
}

android.applicationVariants.all{
    val variant = this
    val name = variant.name
    buildConfigField("boolean", "enableDebugLogging", "false")
    if (name.contains("accountOne")) {
        val googleServices = file("src/accountOne/google-services.json")
        if (!googleServices.exists()) {
            Files.copy(file("src/accountOne/fake-ripple-google-services.json").toPath(), googleServices.toPath())
        }
    }
    if (name.contains("accountTwo")) {
        val googleServices = file("src/accountTwo/google-services.json")
        if (!googleServices.exists()) {
            Files.copy(file("src/accountTwo/fake-scary-google-services.json").toPath(), googleServices.toPath())
        }
    }
}
