
plugins {
  kotlin("jvm")
  kotlin("plugin.spring") version kotlinVersion
  kotlin("plugin.jpa") version kotlinVersion

  id("org.springframework.boot") version DepVers.springBootVersion
  id("io.spring.dependency-management") version DepVers.springBootDependencyManagementVersion
}

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
  mavenCentral()
  jcenter()

  // Gives us access to the spring-kotlinx-serialization library, until
  // https://github.com/spring-projects/spring-framework/pull/24436
  // is merged
  //  maven { url = uri("https://dl.bintray.com/markt-de/snapshots/") }
}

val mainClassKt = "app.backend.Application"

dependencies {
  implementation(project(":app"))

  implementation(platform("org.springframework.boot:spring-boot-dependencies:${DepVers.springBootVersion}"))

  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("com.fasterxml.jackson.core:jackson-annotations")

  // Not yet working, so we rely on Jackson
  //  implementation("de.classyfi.boot:spring-kotlinx-serialization-starter-webflux:0.0.1-20200318")

  implementation("com.soywiz.korlibs.klock:klock:${DepVers.klockVersion}")

  developmentOnly("org.springframework.boot:spring-boot-devtools")

  runtimeOnly("com.h2database:h2")

  implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${DepVers.serializationRuntimeVersion}")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation(kotlin("test-annotations-common"))
  testImplementation(kotlin("test-junit5"))

  testImplementation("org.junit.jupiter:junit-jupiter-api:${TestVers.junitVersion}")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${TestVers.junitVersion}")
}

tasks {
  processResources {
    dependsOn(":app:jsBrowserWebpack")
    from(project(":app").projectDir.resolve("src/commonMain/resources")) {
      into("static")
    }
    from(project(":app").buildDir.resolve("distributions/app.js")) {
      into("static")
    }
  }
}