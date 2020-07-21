package app.model

import dev.fritz2.lenses.Lenses

/**
 * Represents state specific to the UI
 */
@Lenses
// TODO: Move this into the JS source-set
data class ToDoUiState(
    val id: String,
    val editing: Boolean
)