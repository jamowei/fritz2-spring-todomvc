package app.frontend

import app.model.*
import dev.fritz2.binding.*
import dev.fritz2.dom.append
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Keys
import dev.fritz2.dom.html.render
import dev.fritz2.dom.key
import dev.fritz2.dom.states
import dev.fritz2.dom.values
import dev.fritz2.remote.body
import dev.fritz2.remote.onErrorLog
import dev.fritz2.remote.remote
import dev.fritz2.routing.router
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json

data class Filter(val text: String, val function: (List<ToDoBrowser>) -> List<ToDoBrowser>)

val filters = mapOf(
    "/" to Filter("All") { it },
    "/active" to Filter("Active") { toDos -> toDos.filter { it.toDo.status is Uncompleted } },
    "/completed" to Filter("Completed") { toDos -> toDos.filter { it.toDo.status is Completed } }
)

@UnstableDefault
@ExperimentalCoroutinesApi
@FlowPreview
fun main() {
    val router = router("/")
    val api = remote("/api/todos")
    val serializer = ToDo.serializer()

    val toDos = object : RootStore<List<ToDoBrowser>>(listOf(), id = "todos", dropInitialData = true) {

        val load = apply<Unit, List<ToDoBrowser>> { _ ->
            api.get().onErrorLog().body().map { str ->
                Json.parse(serializer.list, str).map { ToDoBrowser(it, false) }
            }
        } andThen handle { _, toDos -> toDos }

        val add = apply<String, ToDoBrowser?> { text ->
            if (text.isNotEmpty()) { //TODO: add validation
                val content = Json.stringify(serializer, ToDo(text = text))
                println(content)
                api.contentType("application/json")
                    .body(content)
                    .post().onErrorLog().body().map {
                        Json.parse(serializer, it).run { ToDoBrowser(this, false) }
                    }
            }
            else flowOf(null)
        } andThen handle { toDos, toDo ->
            if(toDo != null) toDos + toDo else toDos
        }

        val remove = apply<String, String> { id ->
            api.delete(id).onErrorLog().map { id }
        } andThen handle { toDos, id: String ->
            toDos.filterNot { it.toDo.id == id }
        }

        val toggleAll = handle<Boolean> { toDos, toggle ->
            val status = if (toggle) Completed.now() else Uncompleted
            toDos.map { it.copy(toDo = it.toDo.copy(status = status)) }
        }

        val clearCompleted = handle { toDos ->
            toDos.filterNot { it.toDo.status is Completed }
        }

        val count = data.map { todos -> todos.count { it.toDo.status is Uncompleted } }.distinctUntilChanged()
        val allChecked = data.map { todos -> todos.isNotEmpty() && todos.all { it.toDo.status is Completed } }.distinctUntilChanged()

        init {
            action() handledBy load
        }
    }

    val inputHeader = render {
        header {
            h1 { +"todos" }
            input("new-todo") {
                placeholder = const("What needs to be done?")
                autofocus = const(true)

                changes.values().onEach { domNode.value = "" } handledBy toDos.add
            }
        }
    }

    val mainSection = render {
        section("main") {
            input("toggle-all", id = "toggle-all") {
                type = const("checkbox")
                checked = toDos.allChecked

                changes.states() handledBy toDos.toggleAll
            }
            label(`for` = "toggle-all") {
                text("Mark all as complete")
            }
            ul("todo-list") {
                toDos.data.flatMapLatest { all ->
                    router.routes.map { route ->
                        filters[route]?.function?.invoke(all) ?: all
                    }
                }.each{ it.toDo.id }.map { toDo ->
                    val browserStore = toDos.sub(toDo, { it.toDo.id })
                    val toDoStore = browserStore.sub(L.ToDoBrowser.toDo)
                    val textStore = toDoStore.sub(L.ToDo.text)
                    val statusStore = toDoStore.sub(L.ToDo.status)
                    val editingStore = browserStore.sub(L.ToDoBrowser.editing)

                    render {
                        li {
                            attr("data-id", browserStore.id)
                            //TODO: better flatmap over editing and status
                            classMap = browserStore.data.map {
                                mapOf(
                                    "completed" to (it.toDo.status is Completed),
                                    "editing" to it.editing
                                )
                            }
                            div("view") {
                                input("toggle") {
                                    type = const("checkbox")
                                    checked = statusStore.data.map { it is Completed }

                                    changes.states().map { if (it) Completed.now() else Uncompleted } handledBy statusStore.update
                                }
                                label {
                                    textStore.data.bind()

                                    dblclicks.map { true } handledBy editingStore.update
                                }
                                button("destroy") {
                                    clicks.events.map { toDo.toDo.id } handledBy toDos.remove
                                }
                            }
                            input("edit") {
                                value = textStore.data
                                changes.values() handledBy textStore.update

                                editingStore.data.map { isEditing ->
                                    if (isEditing) domNode.apply {
                                        focus()
                                        select()
                                    }
                                    isEditing.toString()
                                }.watch()
                                merge(
                                    blurs.map { false },
                                    keyups.key().filter { it.isKey(Keys.Enter) }.map { false }
                                ) handledBy editingStore.update
                            }
                        }
                    }
                }.bind()
            }
        }
    }

    fun HtmlElements.filter(text: String, route: String) {
        li {
            a {
                className = router.routes.map { if (it == route) "selected" else "" }
                href = const("#$route")
                text(text)
            }
        }
    }

    val appFooter = render {
        footer("footer") {
            span("todo-count") {
                strong {
                    toDos.count.map {
                        "$it item${if (it != 1) "s" else ""} left"
                    }.bind()
                }
            }

            ul("filters") {
                filters.forEach { filter(it.value.text, it.key) }
            }
            button("clear-completed") {
                text("Clear completed")

                clicks handledBy toDos.clearCompleted
            }
        }
    }

    append("todoapp", inputHeader, mainSection, appFooter)
}