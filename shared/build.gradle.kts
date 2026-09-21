plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Asynchronisme et flux réactifs
            implementation(libs.kotlinx.coroutines.core)

            // Gestion de l'état UI et cycle de vie multiplateforme
            implementation(libs.androidx.lifecycle.viewmodel)

            // Manipulation multiplateforme des dates et heures
            implementation(libs.kotlinx.datetime)

            // Ressources CMP (exposées au module :app) et Runtime Compose
            api(compose.components.resources)
            implementation(compose.runtime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
        }

        iosMain.dependencies {
        }
    }
}

android {
    namespace = "com.example.shared"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// Configuration pour autoriser le module 'app' à consommer l'objet Res
compose.resources {
    publicResClass = true
    packageOfResClass = "com.example.shared.resources"
}