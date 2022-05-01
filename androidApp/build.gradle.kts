plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}

val composeVersion = "1.1.1"

android {
    compileSdk = 32
    sourceSets["debug"].java.srcDir(file("build/generated/ksp/debug/kotlin"))
    defaultConfig {
        applicationId = "com.example.bluetoothkmm.android"
        minSdk = 25
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("dev.bluefalcon:blue-falcon:0.9.8")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.1-alpha")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("io.github.raamcosta.compose-destinations:core:1.5.1-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.5.1-beta")

    // official compose navigation
    implementation("androidx.navigation:navigation-compose:2.4.1")

    val koinVersion = "3.1.3"
    implementation("io.insert-koin:koin-android:$koinVersion")
//    implementation("io.insert-koin:koin-android-viewmodel:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
}