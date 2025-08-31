plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)
}

android {
    namespace = "com.example.helloaiadventtemplate"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.helloaiadventtemplate"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    defaultConfig {
        versionName = providers.gradleProperty("VERSION_NAME").orNull ?: "0.0.0"
        versionCode = providers.gradleProperty("VERSION_CODE").orNull?.toInt() ?: 1
    }

    signingConfigs {
        create("release") {
            // В CI мы создаём keystore.jks из секрета; локально файла может не быть
            storeFile = file(System.getenv("SIGNING_STORE_FILE") ?: "keystore.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            // рекомендум для прод-сборок
            isMinifyEnabled = true
            // по желанию: ужать ресурсы вместе с кодом
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Подписываем релиз ТОЛЬКО если заданы пароли (чтобы локально не падало)
            if (!System.getenv("SIGNING_STORE_PASSWORD").isNullOrBlank()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }

        // debug оставляем как есть
        getByName("debug") {
            // настройки по умолчанию
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
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}