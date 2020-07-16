plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")

    id("dev.fritz2.fritz2-gradle") version PluginVers.fritz2GradleVersion
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
    jvm()
    js {
        browser {
//      runTask {
//        devServer = KotlinWebpackConfig.DevServer(
//          open = true,
//          port = 3000,
//          proxy = mapOf("/todo/*" to "http://localhost:8080/todo"),
//          contentBase = listOf("$buildDir/distributions")
//        )
//      }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${DepVers.serializationRuntimeVersion}")
                implementation("com.soywiz.korlibs.klock:klock:${DepVers.klockVersion}")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${DepVers.serializationRuntimeVersion}")
            }
        }

        val jvmTest by getting {
            dependencies {
                dependsOn(commonMain)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit5"))

                implementation("org.junit.jupiter:junit-jupiter-api:${TestVers.junitVersion}")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:${TestVers.junitVersion}")
            }
        }

        val jsMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:${DepVers.serializationRuntimeVersion}")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}