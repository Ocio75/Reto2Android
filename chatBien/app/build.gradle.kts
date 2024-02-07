plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.chatbien"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chatbien"
        minSdk = 24
        targetSdk = 33
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.getkeepsafe.relinker:relinker:1.4.5")
    implementation ("com.twilio:voice-android:5.7.2")
    implementation (  "androidx.emoji:emoji-appcompat:1.1.0")
    implementation ("androidx.core:core-ktx:1.7.0"  )
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation (files("libs/mysql-connector-java-5.1.49.jar"))
    implementation("androidx.emoji2:emoji2:1.4.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
}
