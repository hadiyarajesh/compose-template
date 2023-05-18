plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    kotlin("kapt")
}

android {
    namespace = "com.hadiyarajesh.composetemplate"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.hadiyarajesh.composetemplate"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = LibVersion.composeCompilerVersion
    }
    packaging {
        resources {
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

object LibVersion {
    const val composeVersion = "2023.05.01"
    const val composeCompilerVersion = "1.4.7"
    const val navigationCompose = "2.5.3"
    const val roomVersion = "2.5.1"
    const val retrofitVersion = "2.9.0"
    const val moshiVersion = "1.14.0"
    const val coilVersion = "2.3.0"
    const val flowerVersion = "3.1.0"
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:${LibVersion.composeVersion}")

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.navigation:navigation-compose:${LibVersion.navigationCompose}")

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.dagger:hilt-android:${rootProject.extra["hiltVersion"]}")
    kapt("com.google.dagger:hilt-android-compiler:${rootProject.extra["hiltVersion"]}")

    implementation("androidx.room:room-runtime:${LibVersion.roomVersion}")
    implementation("androidx.room:room-ktx:${LibVersion.roomVersion}")
    ksp("androidx.room:room-compiler:${LibVersion.roomVersion}")

    implementation("com.squareup.retrofit2:retrofit:${LibVersion.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-moshi:${LibVersion.retrofitVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    implementation("com.squareup.moshi:moshi:${LibVersion.moshiVersion}")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:${LibVersion.moshiVersion}")

    implementation("io.coil-kt:coil-compose:${LibVersion.coilVersion}") {
        because("An image loading library for Android backed by Kotlin Coroutines")
    }

    implementation("io.github.hadiyarajesh.flower-retrofit:flower-retrofit:${LibVersion.flowerVersion}") {
        because("Flower simplifies networking and database caching on Android/Multiplatform")
    }

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // UI Tests
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // Android Studio Preview support
    debugImplementation("androidx.compose.ui:ui-tooling")
}

// Pass options to Room ksp processor
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

// Make Kapt-generated stubs to target JDK 17
tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask>().configureEach {
    kotlinOptions.jvmTarget = "17"
}
