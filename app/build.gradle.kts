import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
}

room {
    schemaDirectory("$projectDir/schemas")
}

// Local Properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val apiKeyOpenAI: String = localProperties.getProperty("API_KEY_OPENAI", "")

android {
    namespace = "com.gondroid.noteai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gondroid.noteai"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "API_KEY_OPENAI", "\"${apiKeyOpenAI}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Librerias Android y compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.navigation.compose)

    // https://material-foundation.github.io/material-theme-builder/
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.foundation.android)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Librerias Room
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    // Librerias Dagger Hilt
    implementation(libs.dagger.hilt.navigation.compose)
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)

    // Libreria Serializacion
    implementation(libs.kotlinx.serialization.json)

    // Moshi
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
    implementation(libs.moshi.kotlin)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)


    // testing
    // Pruebas unitarias básicas con JUnit
    testImplementation(libs.junit.v4132)
    // Pruebas con corutinas
    testImplementation(libs.kotlinx.coroutines.test)
    // Librería de mocking (opcional, puedes usar Mockk o Mockito)
    testImplementation(libs.mockk)
    // Room Testing: Permite usar la base de datos en memoria
    testImplementation(libs.androidx.room.testing)
    // Turbine: Para testear Flows y StateFlow de forma reactiva
    testImplementation(libs.app.turbine)
    // robolectric
    testImplementation(libs.robolectric)

    testImplementation(libs.androidx.core) // Para ApplicationProvider
    testImplementation(libs.androidx.core.testing) // Para pruebas de Room


    /**
     * JUnit: Proporciona el framework básico para pruebas unitarias.
     * kotlinx-coroutines-test: Facilita la prueba de corutinas y flujos (Flows) de forma controlada.
     * Turbine: Útil para probar Flow de manera sencilla y declarativa.
     * Mockk: Permite simular (mockear) dependencias y objetos en pruebas unitarias.
     * Room Testing: Ofrece utilidades para probar la integración con Room usando bases de datos en memoria.
     * androidx.test.ext:junit y espresso-core: Son para pruebas instrumentadas en dispositivos o emuladores Android.
     * Compose UI Test: Permite realizar pruebas de UI para componentes construidos con Jetpack Compose.
     */
    testImplementation(kotlin("test"))
}
