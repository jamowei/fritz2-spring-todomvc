plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("dev.fritz2.fritz2-gradle")
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
        implementation(kotlin("stdlib"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${project.extra.get("serializationVersion")}")
        implementation("dev.fritz2:core:${project.extra.get("fritz2Version")}")
      }
    }

    val jsMain by getting {
      dependencies {
        implementation(kotlin("stdlib-js"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:${project.extra.get("serializationVersion")}")
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${project.extra.get("serializationVersion")}")
      }
    }

//    val jsTest by getting {
//      dependencies {
//        implementation(kotlin("test-js"))
//      }
//   }
  }
}