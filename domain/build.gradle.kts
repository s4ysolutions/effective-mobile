plugins {
    id("kotlin-kapt")
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    //kapt(libs.hilt.compiler) must be dagger
    implementation(libs.rxjava)
    // implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.rx3)
    //implementation(libs.hilt)
    testImplementation(libs.junit)
}