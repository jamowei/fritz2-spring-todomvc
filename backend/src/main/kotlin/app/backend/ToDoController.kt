package app.backend

import app.backend.persistence.ToDoRepository
import app.backend.persistence.toEntity
import app.backend.persistence.toModel
import app.model.ToDo
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class ToDoController(val repo: ToDoRepository) {
    @GetMapping
    fun findAll() = repo.findAll().map { it.toModel() }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String) = repo.findById(id.toLong())

    @PostMapping
    fun save(@RequestBody toDo: ToDo) = repo.save(toDo.toEntity()).toModel()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = repo.deleteById(id.toLong())

}