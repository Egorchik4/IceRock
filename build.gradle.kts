// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.42")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:1.7.0")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    }
}


plugins {
    id ("com.android.application") version "7.2.1" apply false
    id ("com.android.library") version "7.2.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.6.20" apply false
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}
