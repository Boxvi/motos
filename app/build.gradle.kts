import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.services)

}

val keystoreProperties = Properties().apply {
    rootProject.file("keystore.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
        ?: println("⚠️ Advertencia: No se encontró el archivo keystore.properties")
}


android {
    namespace = "com.boxvisoft.motos"
    compileSdk = 34

    signingConfigs {
        create("release") {
            val storeFilePath = keystoreProperties["storeFile"]?.toString()
            val storePass = keystoreProperties["storePassword"]?.toString()
            val keyAliasName = keystoreProperties["keyAlias"]?.toString()
            val keyPass = keystoreProperties["keyPassword"]?.toString()

            if (storeFilePath != null && storePass != null && keyAliasName != null && keyPass != null) {
                storeFile = file(storeFilePath)
                storePassword = storePass
                keyAlias = keyAliasName
                keyPassword = keyPass
            } else {
                println("⚠️ No se encontraron todas las propiedades del keystore. Verifica tu keystore.properties")
            }
        }
    }

    defaultConfig {
        applicationId = "com.boxvisoft.motos"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
//    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
}

// ESTO DEBE ESTAR AL FINAL DEL ARCHIVO
