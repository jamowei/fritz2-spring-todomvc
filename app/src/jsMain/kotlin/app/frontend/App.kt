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

data class Filter(val text: String, val function: (List<ToDo>) -> List<ToDo>)

val filters = mapOf(
    "/" to Filter("All") { it },
    "/active" to Filter("Active") { toDos -> toDos.filter { it.status is Uncompleted } },
    "/completed" to Filter("Completed") { toDos -> toDos.filter { it.status is Completed } }
)

@UnstableDefault
@ExperimentalCoroutinesApi
@FlowPreview
fun main() {
    val router = router("/")
    val api = remote("/api/todos")
    val serializer = ToDo.serializer()

    val uiStateStore = object : RootStore<List<TodoTableModel>>(emptyList(), id="uistate", dropInitialData = true) {
        val load = handle { _, states: List<ToDo> ->
            println("Loaded UI states")
            states.map { todo ->
                TodoTableModel(todo.id, false)
            }
        }

        val add = handle { states, state: ToDo ->
            println("Added ${state.id} to uiState")
            states + TodoTableModel(state.id, false)
        }

        val remove = handle { states, id: String ->
            println("Removed ${id} from uiState")
            states.filterNot { it.id == id }
        }
    }

    val toDos = object : RootStore<List<ToDo>>(listOf(), id = "todos", dropInitialData = true) {

        // TODO https://github.com/jwstegemann/fritz2/issues/126
        val loadOfferer = handleAndOffer<List<ToDo>, List<ToDo>> { _, toDos ->
            offer(toDos)
            toDos
        }
        val load = apply<Unit, List<ToDo>> { _ ->
            api.get().onErrorLog().body().map { str ->
                Json.parse(serializer.list, str)
            }
        } andThen loadOfferer

        // TODO https://github.com/jwstegemann/fritz2/issues/126
        val addOfferer = handleAndOffer<ToDo?, ToDo> { state, toDo: ToDo? ->
            if (toDo != null) {
                offer(toDo)
                state + toDo
            } else state
        }

        val add = apply<String, ToDo?> { text ->
            if (text.isNotEmpty()) { //TODO: add validation
                val content = Json.stringify(serializer, ToDo(text = text))
                println(content)
                api.contentType("application/json")
                    .body(content)
                    .post().onErrorLog().body().map {
                        Json.parse(serializer, it)
                    }
            }
            else flowOf(null)
        } andThen addOfferer

        // TODO https://github.com/jwstegemann/fritz2/issues/126
        val removeOfferer = handleAndOffer<String, String> { state, id: String ->
            val newState = state.filterNot { it.id == id }
            offer(id)
            newState
        }
        val remove = apply<String, String> { id ->
            println("Removed TODO $id")
            api.delete(id).onErrorLog().map { id }
        } andThen removeOfferer

        val toggleAll = handle<Boolean> { toDos, toggle ->
            val status = if (toggle) Completed.now() else Uncompleted
            toDos.map { it.copy(status = status) }
        }

        // TODO: Should do a corresponding cleanup of the UI-state store
        val clearCompleted = handle { toDos ->
            toDos.filterNot { it.status is Completed }
        }

        val count = data.map { todos -> todos.count { it.status is Uncompleted } }.distinctUntilChanged()
        val allChecked = data.map { todos -> todos.isNotEmpty() && todos.all { it.status is Completed } }.distinctUntilChanged()

        init {
            action() handledBy load
        }
    }.also {
        // Link events in the main store to the UI state store
        it.loadOfferer handledBy uiStateStore.load
        it.addOfferer handledBy uiStateStore.add
        it.removeOfferer handledBy uiStateStore.remove
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
                }
                // Combine events from the two stores into a Pair
                .combine(uiStateStore.data) { todos, uiState ->
                    val states = uiState.map { it.id to it }.toMap()

                    todos.map { todo ->
                        todo to states.getOrElse(todo.id) {
                            println("Couldn't find UI State for ID ${todo.id}, using default")
                            TodoTableModel(todo.id, false)
                        }
                    }
                }
                .each { it.first.id }.map { combined ->
                    val toDo = combined.first

                    val todoLineStore = toDos.sub(toDo, { it.id })
                    val textStore = todoLineStore.sub(L.ToDo.text)
                    val statusStore = todoLineStore.sub(L.ToDo.status)

                    val stateLineStore = uiStateStore.sub(combined.second, { it.id })
                    val uiEditingStore = stateLineStore.sub(L.TodoTableModel.editing)

                    render {
                        li {
                            attr("data-id", todoLineStore.id)
                            classMap = todoLineStore.data.combine(stateLineStore.data) { todo, state ->
                                mapOf(
                                    "completed" to (todo.status is Completed),
                                    "editing" to state.editing
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

                                    dblclicks.map { true } handledBy uiEditingStore.update
                                }
                                button("destroy") {
                                    clicks.events.map { toDo.id } handledBy toDos.remove
                                }
                            }
                            input("edit") {
                                value = textStore.data
                                changes.values() handledBy textStore.update

                                uiEditingStore.data.map { isEditing ->
                                    if (isEditing) domNode.apply {
                                        focus()
                                        select()
                                    }
                                    isEditing.toString()
                                }.watch()
                                merge(
                                    blurs.map { false },
                                    keyups.key().filter { it.isKey(Keys.Enter) }.map { false }
                                ) handledBy uiEditingStore.update
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