package app.backend.data

import app.model.ToDoValidator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class ToDoController(val repo: ToDoRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private fun doWhenExists(id: Long, run: (ToDoEntity) -> Unit): ResponseEntity<Any> {
        val toDo = repo.findById(id)
        return if (toDo.isPresent) {
            run(toDo.get())
            ResponseEntity.status(HttpStatus.CREATED).build()
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "invalid id"))
        }
    }


    @GetMapping
    fun all(): MutableIterable<ToDoEntity> {
        logger.info("getting all ToDos")
        return repo.findAll()
    }

    @PostMapping
    fun add(@RequestBody toDo: ToDoEntity): ResponseEntity<Any> =
        if (ToDoValidator.isValid(toDo.toToDo(), Unit)) {
            logger.info("save new ToDo: $toDo")
            ResponseEntity.status(HttpStatus.CREATED).body(repo.save(toDo.copy(id = -1)))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "data is not valid"))
        }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody newToDo: ToDoEntity): ResponseEntity<*> =
        if (repo.findById(id).isEmpty) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "invalid id"))
        } else if(!ToDoValidator.isValid(newToDo.toToDo(), Unit)) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "data is not valid"))
        } else {
            logger.info("update ToDo[id=$id] to: $newToDo")
            ResponseEntity.status(HttpStatus.CREATED).body(repo.save(newToDo.copy(id = id)))
        }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        val oldToDo = repo.findById(id)
        return if(oldToDo.isEmpty) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "invalid id"))
        } else {
            logger.info("remove ToDo with id: $id")
            repo.deleteById(id)
            ResponseEntity.status(HttpStatus.OK).build()
        }
    }

}