package app.backend.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import javax.persistence.*

@Entity
@Table(name = "Todos")
data class ToDoEntity(
        @Id
        @JsonSerialize(using = ToStringSerializer::class)
        @JsonDeserialize(`as` = Long::class)
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1,
        val text: String = "",
        val completed: Boolean = false,
        val editing: Boolean = false
) {
}