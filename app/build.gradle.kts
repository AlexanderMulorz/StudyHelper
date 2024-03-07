import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.chaquo.python")
}

android {
    namespace = "com.common.studyhelper"
    compileSdk = 34




    defaultConfig {
        applicationId = "com.common.studyhelper"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"



        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a", "x86_64")
        }

        chaquopy {
            sourceSets.getByName("main") {
                setSrcDirs(listOf("src/main/python"))
            }
            defaultConfig {
                buildPython("C:\\Users\\Alexander\\PycharmProjects\\testProject\\venv\\Scripts\\python.exe")
            }
            defaultConfig{
                pip{
                    install("youtube-transcript-api")
                }
            }
        }


        // Get the API keys from local.properties

    }


    // Get the API keys from local.properties
    val GPT_API_KEY: String = gradleLocalProperties(rootDir).getProperty("GPT_API_KEY")



    buildTypes {
        debug {
            buildConfigField("String", "GPT_API_KEY", GPT_API_KEY)
        }
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
        viewBinding = true
        dataBinding = true
        buildConfig = true

    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.aallam.openai:openai-client:3.7.0")
}

