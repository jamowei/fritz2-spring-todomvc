plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("dev.fritz2.fritz2-gradle") version "0.6"
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

    val serialization_version = "0.20.0"

    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serialization_version")
      }
    }

    val jsMain by getting {
      dependencies {
        implementation(kotlin("stdlib-js"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serialization_version")
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serialization_version")
      }
    }

//    val jsTest by getting {
//      dependencies {
//        implementation(kotlin("test-js"))
//      }
//   }
  }
}