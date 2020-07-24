package app.backend.data

import app.model.ToDo
import javax.persistence.*

@Entity
@Table(name = "Todos")
data class ToDoEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1,
        val text: String = "",
        val completed: Boolean = false
) {
        fun toToDo() = ToDo(id, text, completed)
}