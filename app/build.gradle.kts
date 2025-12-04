import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.compose.compiler)
}

// Leer propiedades locales
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "com.hugoguerrero.tecno"
    compileSdk = 36

    signingConfigs {
        create("release") {
            keyAlias = localProperties.getProperty("keyAlias")
            keyPassword = localProperties.getProperty("keyPassword")
            storeFile = file(localProperties.getProperty("storeFile"))
            storePassword = localProperties.getProperty("storePassword")
        }
    }

    defaultConfig {
        applicationId = "com.hugoguerrero.tecno"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11" // Versión compatible
    }

    kotlin {
        jvmToolchain(17)
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {

    // --- Compose BOM ---
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Navigation Compose
    implementation(libs.androidx.compose.navigation)

    // --- Firebase BOM ---
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.functions.ktx)
    implementation(libs.firebase.appcheck.ktx)
    implementation(libs.firebase.appcheck.playintegrity)
    debugImplementation(libs.firebase.appcheck.debug) // <-- AÑADIDO

    // Firestore Offline Persistence
    implementation(libs.coroutines.play.services)

    // --- Hilt + KSP ---
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.text)
    ksp(libs.hilt.compiler)

    // ... tus otras dependencias (Compose, Firebase, Hilt, etc.)// AÑADE ESTA LÍNEA para solucionar el error de reflexión
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.24") // Asegúrate de que la versión coincida con la de tu proyecto

    // App Check
    implementation("com.google.firebase:firebase-appcheck-debug")


    // Debug tools (Referencias corregidas)
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // --- TESTING ---
    testImplementation(libs.junit)

    //Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("io.coil-kt:coil-compose:2.7.0")

    //Google
    implementation("com.google.android.gms:play-services-auth:21.4.0")

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
