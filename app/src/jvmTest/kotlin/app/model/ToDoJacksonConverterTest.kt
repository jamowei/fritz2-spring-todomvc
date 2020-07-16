package app.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.soywiz.klock.annotations.KlockExperimental
import com.soywiz.klock.wrapped.WDateTime
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@KlockExperimental
@OptIn(UnstableDefault::class)
class ToDoJacksonConverterTest {

    private val mapper = ObjectMapper()
        .also { it.registerModule(KotlinModule()) }
        .also { it.registerModule(TodoJacksonModule) }

    @Test
    fun `test that Jackson serialization is equivalent to kotlinx serialization for Completed`() {
        val todoCompleted = ToDo("12", "abc", Completed(WDateTime.now()), false)

        assertEquals(Json.stringify(ToDo.serializer(), todoCompleted), mapper.writeValueAsString(todoCompleted))
    }

    @Test
    fun `test that Jackson serialization is equivalent to kotlinx serialization for Uncompleted`() {
        val todoUncompleted = ToDo("12", "abc", Uncompleted, false)

        assertEquals(Json.stringify(ToDo.serializer(), todoUncompleted), mapper.writeValueAsString(todoUncompleted))
    }

    @Test
    fun `test that Jackson and kotlinx deserialize Uncompleted in the same way`() {
        val uncompleted = ToDo("12", "abc", Uncompleted, false)
        val str = Json.stringify(ToDo.serializer(), uncompleted)

        // Round-trip jackson
        assertEquals(mapper.readValue(str, ToDo::class.java), uncompleted)
        // Round-trip kotlinx
        assertEquals(Json.parse(ToDo.serializer(), str), uncompleted)
    }

//    @Test
//    fun `test that Jackson and kotlinx deserialize Completed in the same way`() {
//        val completed = ToDo("12", "abc", Completed.now(), false)
//        val str = Json.stringify(ToDo.serializer(), completed)
//
//        // Round-trip jackson
//        val jackson = mapper.readValue(str, ToDo::class.java)
//
//        val jacksonDate = (jackson.status as Completed).completedOn
//        val kotlinDate = (completed.status as Completed).completedOn
//
//
//        assertEquals(jackson, completed)
//
//        // Round-trip kotlinx
//        val kotlinx = Json.parse(ToDo.serializer(), str)
//        assertEquals(kotlinx, completed)
//    }
}