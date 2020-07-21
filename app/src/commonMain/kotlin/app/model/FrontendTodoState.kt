package app.model

import dev.fritz2.lenses.Lenses

/**
 * Represents state specific to the TodoTable widget
 */
@Lenses
// TODO: Move this into the JS source-set
data class TodoTableState(
    val id: String,
    val editing: Boolean
)