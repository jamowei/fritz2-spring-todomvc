package dev.fritz2.backend.todo

import dev.fritz2.backend.data.ToDoData
import dev.fritz2.frontend.model.ToDo
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/todo")
class ToDoController(val repository: ToDoRepository) {

    private val log = KotlinLogging.logger {}

    @GetMapping
    fun all(): MutableIterable<ToDoData> = repository.findAll()

//    @PostMapping
//    fun save(quote: Quote)

}