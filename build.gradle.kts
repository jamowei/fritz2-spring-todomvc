allprojects {
  group = "dev.fritz2"
  version = "1.0"
}

subprojects {
  repositories {
    maven("https://oss.jfrog.org/artifactory/jfrog-dependencies")
    jcenter()
    maven("https://dl.bintray.com/jwstegemann/fritz2")
    mavenCentral()
  }

  extra.set("serializationVersion", "1.1.0")
}

plugins {
  val fritz2Version = "0.8"
  val kotlinVersion = "1.4.30"
  val springBootVersion = "2.4.0"
  val springBootDependencyManagementVersion = "1.0.10.RELEASE"
  id("dev.fritz2.fritz2-gradle") version fritz2Version apply false
  kotlin("multiplatform") version kotlinVersion apply false
  kotlin("js") version kotlinVersion apply false
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.spring") version kotlinVersion apply false
  kotlin("plugin.jpa") version kotlinVersion apply false
  kotlin("plugin.serialization") version kotlinVersion apply false
  id("org.springframework.boot") version springBootVersion apply false
  id("io.spring.dependency-management") version springBootDependencyManagementVersion apply false
}