package app.backend.data

import org.springframework.data.repository.CrudRepository

interface ToDoRepository : CrudRepository<ToDoEntity, Long> {
//    fun findByCompleted(completed: Boolean): List<ToDoData>
}