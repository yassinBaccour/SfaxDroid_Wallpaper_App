import java.util.Locale

buildscript {


    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath(libs.gradle.build.tool)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.google.service)
        classpath(libs.hilt.gradle.plugin)
        classpath(libs.spotless)
        classpath(libs.lib.check.update)
        classpath(libs.google.crashlytics.gradle)
    }
}

apply(plugin = "com.github.ben-manes.versions")

allprojects {
    repositories {
        jcenter()
        maven("https://jitpack.io")
        maven("https://maven.google.com")
        google()
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            target("**/*.java")
            googleJavaFormat().aosp()
            removeUnusedImports()
            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
        kotlin {
            target ("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")
            ktlint(libs.versions.ktlint.get())
            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
        format("misc") {
            target("**/*.gradle", "**/*.md", "**/.gitignore")
            indentWithSpaces()
            trimTrailingWhitespace()
            endWithNewline()
        }

        format("xml") {
            target("**/*.xml")
            indentWithSpaces()
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase(Locale.getDefault())
        .contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}