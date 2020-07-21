import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
  group = "dev.fritz2"
  version = "0.0.1-SNAPSHOT"
}

plugins {
  kotlin("multiplatform") version kotlinVersion apply false
  kotlin("js") version kotlinVersion apply false
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.serialization") version kotlinVersion apply false

  id("dev.fritz2.fritz2-gradle") version PluginVers.fritz2GradleVersion
}

subprojects {
  tasks {
    withType<Test> {
      useJUnitPlatform()
    }

    withType<KotlinCompile> {
      kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xopt-in=kotlin.RequiresOptIn")
        jvmTarget = "11"
      }
    }
  }
}