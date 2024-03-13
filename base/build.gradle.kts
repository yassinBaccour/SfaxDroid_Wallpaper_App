plugins{
    `java-library`
    kotlin("jvm")
}
dependencies {
    api(libs.kotlin.stdlib)
    api(libs.kotlinx.coroutines.core)
    api(libs.dagger)
}