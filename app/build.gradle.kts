plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("dev.fritz2.fritz2-gradle")
}

kotlin {
  jvm()
  js(IR) {
    browser {
      runTask {
        devServer = devServer?.copy(
          port = 9000,
          proxy = mutableMapOf(
            "/api/todos" to "http://localhost:8080"
          )
        )
      }
    }
  }.binaries.executable()

  sourceSets {

    val commonMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${project.extra["serializationVersion"]}")
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-common"))
        implementation(kotlin("test-junit"))
        implementation(kotlin("test-annotations-common"))
      }
    }

    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}