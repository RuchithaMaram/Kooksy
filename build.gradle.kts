// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin) // Safe Args
        classpath(libs.kotlin.gradle.plugin)
    }
}
//To resolve Safe Args issues, I used ChatGPT, for the correct version
// of the Safe Args plugin is used, and Manage dependency conflicts to avoid build issues
// https://developer.android.com/guide/navigation/use-graph/pass-data#Safe-args
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
