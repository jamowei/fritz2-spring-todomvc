package app.backend.persistence

import javax.persistence.*

@Entity
@Table(name = "Todos")
data class ToDoEntity(
    @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1,
    val text: String = "",
    val status: ToDoStatusEntity = ToDoStatusEntity.UncompletedStatus,
    val completedDate: Long? = null,
    val editing: Boolean = false
)

enum class ToDoStatusEntity {
        CompletedStatus, UncompletedStatus
}
