package app.backend

import app.backend.persistence.ToDoRepository
import app.backend.persistence.toEntity
import app.backend.persistence.toModel
import app.model.ToDoValidator
import app.model.UnvalidatedToDo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class ToDoController(val repo: ToDoRepository) {
    @GetMapping
    fun findAll() = repo.findAll().map { it.toModel() }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String) = repo.findById(id.toLong())

    @PostMapping
    fun save(@RequestBody toDo: UnvalidatedToDo): ResponseEntity<*> {
        val validationMessages = ToDoValidator.validate(toDo, null)

        if (validationMessages.isEmpty()) {
            val todo = repo.save(toDo.toValidated().toEntity()).toModel()
            return ResponseEntity.ok(todo)
        }

        return ResponseEntity.status(400).body(validationMessages)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = repo.deleteById(id.toLong())

}