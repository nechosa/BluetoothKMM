buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        //TODO: update to Kotlin 1.6.20 when Compose will be compatible with it
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        //classpath("org.jetbrains.kotlin:kotlin-serialization:1.6.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}