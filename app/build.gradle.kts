plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkPreview = "S"
    buildToolsVersion = "31.0.0 rc4"

    defaultConfig {
        applicationId = "myui.ui"
        minSdk = 21
        targetSdkPreview = "S"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
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
        freeCompilerArgs = listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-SNAPSHOT"
    }
}

dependencies {
    val compose = "1.0.0-SNAPSHOT"
    val accompanist = "0.10.0"
    implementation("androidx.activity:activity-compose:1.3.0-SNAPSHOT")
    implementation("androidx.appcompat:appcompat:1.4.0-alpha01")
    implementation("androidx.compose.ui:ui:$compose")
    implementation("androidx.compose.material:material:$compose")
    implementation("androidx.compose.material:material-icons-extended:$compose")
    implementation("androidx.compose.ui:ui-tooling:$compose")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha07")
    implementation("androidx.core:core-ktx:1.6.0-SNAPSHOT")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-SNAPSHOT")
    implementation("com.google.accompanist:accompanist-coil:$accompanist")
    implementation("com.google.accompanist:accompanist-flowlayout:$accompanist")
    implementation("com.google.accompanist:accompanist-insets:$accompanist")
    implementation("com.google.accompanist:accompanist-pager:$accompanist")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanist")
    implementation("com.google.android.material:material:1.4.0-beta01")
}