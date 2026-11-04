plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.rishu.tvos"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.rishu.tvos"
        minSdk = 23
        targetSdk = 32
        versionCode = 3
        versionName = "3.0"
    }
}