package app.backend.persistence

import org.springframework.data.repository.CrudRepository

interface ToDoRepository : CrudRepository<ToDoEntity, Long> {
    fun findByStatus(status: ToDoStatusEntity): List<ToDoEntity>
}