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
  js().browser()

  sourceSets {

    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${project.extra.get("serializationVersion")}")
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }

    val jsMain by getting {
      dependencies {
        implementation(kotlin("stdlib-js"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:${project.extra.get("serializationVersion")}")
      }
    }

    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${project.extra.get("serializationVersion")}")
      }
    }
  }
}