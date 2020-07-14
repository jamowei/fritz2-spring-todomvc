package app.model

import dev.fritz2.lenses.Lenses
import kotlinx.serialization.Serializable

@Lenses
@Serializable
data class ToDo(
        val id: String = "-1",
        val text: String = "",
        val completed: Boolean = false,
        val editing: Boolean = false
)