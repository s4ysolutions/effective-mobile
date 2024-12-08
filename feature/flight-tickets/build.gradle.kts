plugins {
    id("kotlin-kapt")
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "solutions.s4y.effectivem.flight_tickets"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    kapt(libs.hilt.compiler)
    implementation(project(":views"))
    implementation(project(":domain"))
    implementation(project(":data:tickets"))
    // needed only for navigation, should be replaced with the dynamic links
    implementation(project(":feature:hotels"))
    implementation(project(":feature:profile"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.hilt.android)
    implementation(libs.material)
    //api("androidx.navigation:navigation-fragment-ktx:2.8.4")
    // api("androidx.navigation:navigation-ui-ktx:2.8.4")
    //api("androidx.navigation:navigation-dynamic-features-fragment:2.8.4")
    testImplementation(libs.junit)
    testImplementation(libs.rxjava)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}