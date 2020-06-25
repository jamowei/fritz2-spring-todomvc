package dev.fritz2.frontend.model

import dev.fritz2.lenses.Lenses
import dev.fritz2.lenses.WithId

//import dev.fritz2.lenses.Lenses


@Lenses
data class ToDo(
    val text: String,
    val completed: Boolean = false,
    val editing: Boolean = false,
    val number: Long = 0,
    override val id: String = number.toString()
) : WithId