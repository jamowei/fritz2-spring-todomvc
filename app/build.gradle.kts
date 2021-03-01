plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("dev.fritz2.fritz2-gradle")
}

kotlin {
  jvm()
  js().browser()

  sourceSets {

    val commonMain by getting {
      dependencies {
        implementation("dev.fritz2:core:0.9-SNAPSHOT")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${project.extra.get("serializationVersion")}")
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
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