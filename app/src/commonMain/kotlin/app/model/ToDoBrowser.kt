package app.model

import dev.fritz2.lenses.Lenses
import dev.fritz2.lenses.idProvider
import kotlinx.serialization.Serializable

@Lenses
@Serializable
data class ToDoBrowser(val toDo: ToDo, val editing: Boolean) {
    companion object {
        val idProvider: idProvider<ToDoBrowser> = { toDo -> toDo.toDo.id }
    }
}