allprojects {
  group = "dev.fritz2"
  version = "0.0.1-SNAPSHOT"
}

plugins {
  val kotlinVersion = "1.3.72"
  val springBootVersion = "2.3.1.RELEASE"
  val springBootDependencyManagementVersion = "1.0.9.RELEASE"
  kotlin("multiplatform") version kotlinVersion apply false
  kotlin("js") version kotlinVersion apply false
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.spring") version kotlinVersion apply false
  kotlin("plugin.jpa") version kotlinVersion apply false
  kotlin("plugin.serialization") version kotlinVersion apply false
  id("org.springframework.boot") version springBootVersion apply false
  id("io.spring.dependency-management") version springBootDependencyManagementVersion apply false
}