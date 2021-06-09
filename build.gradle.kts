allprojects {
  group = "dev.fritz2"
  version = "1.0"
}

subprojects {
  repositories {
    mavenCentral()
  }

  extra.set("serializationVersion", "1.2.1")
  extra.set("fritz2Version", "0.11")
}

plugins {
  val kotlinVersion = "1.5.10"
  val springBootVersion = "2.5.0"
  val springBootDependencyManagementVersion = "1.0.11.RELEASE"
  id("dev.fritz2.fritz2-gradle") version "0.11" apply false
  kotlin("multiplatform") version kotlinVersion apply false
  kotlin("js") version kotlinVersion apply false
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.spring") version kotlinVersion apply false
  kotlin("plugin.jpa") version kotlinVersion apply false
  kotlin("plugin.serialization") version kotlinVersion apply false
  id("org.springframework.boot") version springBootVersion apply false
  id("io.spring.dependency-management") version springBootDependencyManagementVersion apply false
}