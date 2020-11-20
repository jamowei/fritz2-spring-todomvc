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
        //FIXME: remove before release
        implementation("dev.fritz2:core:0.8-SNAPSHOT")
        implementation(kotlin("stdlib"))
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