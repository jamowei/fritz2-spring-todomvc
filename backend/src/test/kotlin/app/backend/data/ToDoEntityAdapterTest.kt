package app.backend.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ToDoEntityAdapterTest {

    @Test
    fun `test that an uncompleted entity can be converted to the application model and back again`() {
        val entity = ToDoEntity(50, "abc", ToDoStatusEntity.UncompletedStatus, null, false)
        
        assertEquals(entity, entity.toModel().toEntity())
    }
}