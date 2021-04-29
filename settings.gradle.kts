rootProject.name = "fritz2-spring-todomvc"

pluginManagement {
    plugins {
        id("dev.fritz2.fritz2-gradle") version "0.10"
    }
    resolutionStrategy {
    }
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

include("app", "backend")