package app.model

import dev.fritz2.lenses.Lenses

@Lenses
data class TodoTableModel(
    val id: String,
    val editing: Boolean
)