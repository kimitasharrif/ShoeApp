plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.sherrif.sneakerhub"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sherrif.sneakerhub"
        minSdk = 28
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //LoopJ for API Connections
    implementation ("com.loopj.android:android-async-http:1.4.9")
    // For JSON Conversions, From JSONArray to ArrayList
    implementation ("com.google.code.gson:gson:2.8.7")
    //For Swipe Refresh in Recycler View
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    //For making rounded circular imageView
    implementation("de.hdodenhof:circleimageview:3.0.1")
    //Location
    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    annotationProcessor ("com.github.bumptech.glide:compiler:<version>")


}