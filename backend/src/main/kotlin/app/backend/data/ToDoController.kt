package app.backend.data

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class ToDoController(val repo: ToDoRepository) {
    @GetMapping
    fun findAll() = repo.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String) = repo.findById(id.toLong())

    @PostMapping
    fun save(@RequestBody toDoEntity: ToDoEntity) = repo.save(toDoEntity)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = repo.deleteById(id.toLong())

}