import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.room)
    alias(libs.plugins.screenshot)
}

android {
    namespace = "com.hadiyarajesh.composetemplate"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.hadiyarajesh.composetemplate"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // Enable Compose Preview screenshot testing (previews under src/screenshotTest).
    // Required by the com.android.compose.screenshot plugin.
    @Suppress("UnstableApiUsage")
    experimentalProperties["android.experimental.enableScreenshotTest"] = true

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

/*
 * Generates Compose compiler metrics and recomposition reports.
 * Enable with: ./gradlew assembleRelease -PenableComposeCompilerReports
 * Reports are written to build/compose_compiler
 */
composeCompiler {
    if (project.hasProperty("enableComposeCompilerReports")) {
        val reportsDir = layout.buildDirectory.dir("compose_compiler")
        reportsDestination = reportsDir
        metricsDestination = reportsDir
    }
}

// Hilt bundles an outdated kotlin-metadata-jvm version.
// Force a version compatible with the current Kotlin release.
configurations.configureEach {
    resolutionStrategy {
        force(libs.kotlin.metadata.jvm)
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)

    implementation(libs.androidx.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.bundles.lifecycle)
    implementation(composeBom)
    implementation(libs.bundles.compose.ui.impl)
    implementation(libs.material3)
    implementation(libs.navigation.compose)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    implementation(libs.bundles.retrofit)
    implementation(libs.okhttp.interceptor.logging)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.coil)

    testImplementation(platform(libs.junit5.bom))
    testImplementation(libs.junit5)
    testRuntimeOnly(libs.junit5.platform.launcher)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(composeBom)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.bundles.compose.ui.debug)

    // Compose Preview screenshot testing
    screenshotTestImplementation(composeBom)
    screenshotTestImplementation(libs.ui.tooling)
}

// Use the JUnit5 Platform for running tests
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
