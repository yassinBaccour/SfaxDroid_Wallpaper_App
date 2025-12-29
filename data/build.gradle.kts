plugins {
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    implementation(projects.domain)
    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gsonConverter)
}
