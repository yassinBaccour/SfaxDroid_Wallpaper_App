plugins {
    `java-library`
    kotlin("jvm")
    id("kotlin-kapt")
}

dependencies {
    kapt(libs.hilt.compiler)
    api(libs.retrofit2.converter)
    api(libs.annotation)
    api(libs.kotlinx.coroutines.core)
    api(libs.room.common)
    api(libs.dagger.dagger)
}