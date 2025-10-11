import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs.kotlin")
//    id("com.google.android.gms.oss-licenses-plugin")
    id("kotlin-parcelize")
    id("org.jlleitschuh.gradle.ktlint")

//    id("org.jetbrains.dokka")
}

android {
    namespace = "com.constant.everydayjapanese"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.constant.everydayjapanese"
        minSdk = 28
        targetSdk = 35
        versionCode = 16
        versionName = "1.00.16"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        create("release") {
        }
    }
    buildTypes {
        getByName("debug") {
            isShrinkResources = false
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "IS_DEBUG", "true")
        }
        getByName("release") {
            signingConfig = signingConfigs["release"]
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "IS_DEBUG", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.activity:activity:1.8.0")
    val lifecycle = "2.6.2"
    val room = "2.6.1"
    val coil = "2.5.0"

    // UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.customview:customview-poolingcontainer:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-common-ktx:3.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")

    // page indicator
    implementation("me.relex:circleindicator:2.1.6")

    // material
    implementation("com.google.android.material:material:1.11.0")

    // bottom sheet
    implementation("com.github.michael-rapp:android-bottom-sheet:2.0.0")

    // 사진 여러장을 pinch-out, pinch-in 좌우로 넘김
    // implementation("com.github.chrisbanes:PhotoView:2.3.0")

    // 이미지
    // implementation("io.coil-kt:coil:$coil")
    // implementation("io.coil-kt:coil-svg:$coil")
    // implementation("io.coil-kt:coil-gif:$coil")

    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Lottie
    implementation("com.airbnb.android:lottie:6.3.0")

    // Reactive X
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.7")

    // Rest API
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ETC
//    implementation("com.google.android.gms:play-services-oss-licenses:17.0.1")

    // 비디오 플레이어
    // implementation("com.google.android.exoplayer:exoplayer:2.19.1")

    // JSON
    implementation("com.google.code.gson:gson:2.10")

    // firebase
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.1")

    // 구글 로그인 관련
    // implementation("com.google.android.gms:play-services-auth:20.2.0")

    // 카카오 로그인
    // implementation("com.kakao.sdk:v2-user:2.19.0")

    // 네이버 로그인
    // implementation("com.navercorp.nid:oauth-jdk8:5.9.0") // jdk 8

    // 구글맵
    // implementation("com.google.android.gms:play-services-maps:18.0.2")
    // implementation("com.google.android.gms:play-services-location:19.0.1")

    // Graph
    // implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Balloon
    // implementation("com.github.skydoves:balloon:1.6.11")

    // Skeleton View
    // implementation("com.facebook.shimmer:shimmer:0.5.0")

    // TTS
    // implementation("android.speech.tts.TextToSpeech")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Compose (나중에 사용할 경우 활성화)
//    implementation("androidx.activity:activity-compose:1.8.2")
//    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

fun getApiKey(propertyKey: String): String {
    val properties = gradleLocalProperties(rootDir, providers)
    return properties.getProperty(propertyKey)
}

// 코드 이뿌게
// ./gradlew ktlintFormat
