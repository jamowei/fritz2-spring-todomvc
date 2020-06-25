package dev.fritz2.backend

import dev.fritz2.backend.data.ToDoData
import dev.fritz2.backend.todo.ToDoRepository
import dev.fritz2.frontend.model.ToDo
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
@Controller
@EntityScan("dev.fritz2.frontend.model")
class Application {

    @GetMapping
    fun start(): String = "index.html"

    // fill db with some data on startup
    @Bean
    fun fillDB(repository: ToDoRepository) = CommandLineRunner {
        repository.save(ToDoData("making fritz2 great"))
        repository.save(ToDoData("fixed as much bugs as possible"))
        repository.save(ToDoData("build some new examples with fritz2"))
        repository.save(ToDoData("produce better code"))
    }

}

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}