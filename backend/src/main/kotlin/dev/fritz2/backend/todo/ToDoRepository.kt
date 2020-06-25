package dev.fritz2.backend.todo

import dev.fritz2.backend.data.ToDoData
import dev.fritz2.frontend.model.ToDo
import org.springframework.data.repository.CrudRepository


interface ToDoRepository : CrudRepository<ToDoData, Long> {
    fun findByCompleted(completed: Boolean): List<ToDoData>
}