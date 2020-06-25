package dev.fritz2.backend.data

import dev.fritz2.frontend.model.ToDo
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ToDoData(
    override val text: String,
    override val completed: Boolean = false,
    override val editing: Boolean = false,
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    override val number: Long = 0) : ToDo(text, completed, editing, number)