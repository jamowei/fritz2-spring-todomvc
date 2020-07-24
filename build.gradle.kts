allprojects {
  group = "dev.fritz2"
  version = "0.0.1-SNAPSHOT"
}



subprojects {


  repositories {
    jcenter()
    mavenCentral()
    // TODO: remove if fritz2 0.7 is released
    maven("https://oss.jfrog.org/artifactory/jfrog-dependencies")
  }

  extra.set("fritz2Version", "0.7-SNAPSHOT")
  extra.set("serializationVersion", "0.20.0")
}

plugins {
  val fritz2Version = "0.6"
  val kotlinVersion = "1.3.72"
  val springBootVersion = "2.3.1.RELEASE"
  val springBootDependencyManagementVersion = "1.0.9.RELEASE"
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