import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.googleFirebaseService) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.navigationSafeArgs) apply false
    alias(libs.plugins.googleFirebasePerformance) apply false
}