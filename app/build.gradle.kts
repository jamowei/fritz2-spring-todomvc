import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

//buildscript {
//  repositories {
//    mavenLocal() //FIXME: nicht einchecken
//    jcenter()
//  }
//
//  dependencies {
//    classpath(kotlin("gradle-plugin"))
//    classpath("dev.fritz2:fritz2-gradle-plugin:0.6")
//  }
//}

plugins {
  kotlin("multiplatform")
//  kotlin("plugin.serialization")
//  id("org.springframework.boot")
//  id("io.spring.dependency-management")
  id("dev.fritz2.fritz2-gradle") version "0.5"
}

//apply(plugin = "dev.fritz2.fritz2-gradle")

repositories {
  mavenCentral()
  jcenter()
}

kotlin {
  jvm()
  js {
    browser {
      runTask {
        devServer = KotlinWebpackConfig.DevServer(
          open = true,
          port = 3000,
          proxy = mapOf("/todo/*" to "http://localhost:8080/todo"),
          contentBase = listOf("$buildDir/distributions")
        )
      }
    }
  }

  sourceSets {

//    all {
//      languageSettings.progressiveMode = true
//    }

    val serialization_version = "0.20.0"

    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib"))
//        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serialization_version")
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation(kotlin("stdlib-jdk8"))
//        api("org.springframework.boot:spring-boot-starter-data-jpa")
//        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serialization_version")
      }
    }
//    val jvmTest by getting {
//      dependencies {
//        implementation(kotlin("test"))
//        implementation(kotlin("test-junit"))
//      }
//    }

    val jsMain by getting {
      dependencies {
//        implementation(kotlin("stdlib-js"))
//        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serialization_version")
      }
    }

//    val jsTest by getting {
//      dependencies {
//        implementation(kotlin("test-js"))
//      }
//    }
  }
}