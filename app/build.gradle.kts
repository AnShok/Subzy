import java.util.Properties

// Загружаем local.properties
val localProperties = Properties().apply {
    file("../local.properties").inputStream().use { load(it) }
}

// Получаем значение LOGO_API_KEY
val logoApiKey = localProperties.getProperty("LOGO_API_KEY") ?: throw GradleException("LOGO_API_KEY not found in local.properties")
val logoSecretKey = localProperties.getProperty("LOGO_SECRET_KEY") ?: throw GradleException("LOGO_SECRET_KEY not found in local.properties")


plugins {
    id("com.android.application") version "8.5.2"
    id("org.jetbrains.kotlin.android") version "2.0.0"
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"  // Для Room с использованием KSP
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin") version "2.7.7"
}

android {
    namespace = "com.anshok.subzy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.anshok.subzy"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // LOGO_API в BuildConfig
        //buildConfigField("String", "LOGO_API_KEY", "\"${logoApiKey}\"")
        buildConfigField("String", "LOGO_SECRET_KEY", "\"${logoSecretKey}\"")

        buildConfigField("String", "APPLICATION_ID", "\"$applicationId\"")

        // Путь для экспорта схемы Room
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas"
                )
            }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    // Основные библиотеки Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.fragment.ktx)

    // Room для работы с базой данных
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.core.ktx)
    implementation(libs.androidx.junit.ktx)
    ksp(libs.androidx.room.compiler)

    // UI библиотеки
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.glide)

    // Сетевые библиотеки и JSON обработка
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)

    // Dependency Injection
    implementation(libs.koin.android)

    // Jetpack Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // ViewBinding Property Delegate
    implementation(libs.viewbindingpropertydelegate.full)
    implementation(libs.viewbindingpropertydelegate.noreflection)

    // Тестирование
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Тестирование
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:4.0.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")


    // Интеграционные тесты
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation ("androidx.room:room-testing:2.4.2")
    // Mockito для интеграционных тестов (androidTest)
    androidTestImplementation ("org.mockito:mockito-android:4.0.0")
    androidTestImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")

    // Circular ProgressBar
    implementation(libs.circularprogressbar)

    implementation (libs.bio.matic.segmentedarcview)
    implementation ("com.google.android.material:material:1.9.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")

    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
}