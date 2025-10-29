plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.vanniktech.maven.publish")
    id("binary-compatibility-validator")
    id("com.diffplug.spotless")
    id("org.jetbrains.dokka")
    id("build-support")
}

val openAiKotlinBuildNonJvm: String? by project

kotlin {
    explicitApi()
    jvm()

    if (openAiKotlinBuildNonJvm == "true") {
        jsNode()
        jsWasm()
        native()
    }

    sourceSets {
        all {
            languageSettings.apply{
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
            }
        }
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.io.core)
                api(libs.serialization.json)
                implementation(libs.serialization.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        if (openAiKotlinBuildNonJvm == "true") {
            val jsTest by getting {
                dependencies {
                    implementation(kotlin("test-js"))
                }
            }
            val wasmJsTest by getting {
                dependencies {
                    implementation(kotlin("test-wasm-js"))
                }
            }
        }
    }
}
