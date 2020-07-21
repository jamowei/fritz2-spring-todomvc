package app.model

import com.soywiz.klock.annotations.KlockExperimental
import com.soywiz.klock.wrapped.WDateTime
import dev.fritz2.lenses.Lenses
import dev.fritz2.validation.ValidationMessage
import dev.fritz2.validation.Validator
import kotlinx.serialization.*


@Lenses
@Serializable
data class ToDo(
        val id: String = "-1",
        val text: String = "",
        val status: ToDoStatus = Uncompleted
)

/**
 * Represents the object in an unvalidated state
 */
data class UnvalidatedTodo(
        val id: String = "-1",
        val text: String,
        val status: ToDoStatus = Uncompleted
) {
        fun toValidated() = ToDo(id, text, status)
}

@Lenses
@Serializable
sealed class ToDoStatus

/**
 * We use the WrappedDateTime class from Klock, because it kotlinx.serialization doesn't yet support inline classes
 */
@Lenses
@Serializable
@OptIn(KlockExperimental::class)
data class Completed(
        @Serializable(with = DateTimeSerializer::class) val completedOn: WDateTime
): ToDoStatus() {

        companion object {
                fun now() = Completed(WDateTime.now())
        }
}

@Lenses
@Serializable
object Uncompleted: ToDoStatus()


// TODO: Figure out why a custom Serializer is required
@OptIn(KlockExperimental::class)
object DateTimeSerializer : KSerializer<WDateTime> {
        override val descriptor: SerialDescriptor
                get() = PrimitiveDescriptor("DateTime", PrimitiveKind.DOUBLE)

        override fun deserialize(decoder: Decoder) = WDateTime.fromUnix(decoder.decodeLong())
        override fun serialize(encoder: Encoder, value: WDateTime) = encoder.encodeLong(value.unixMillisLong)
}

inline class TodoValidatorMessage(val msg : String): ValidationMessage {
        override fun failed(): Boolean {
                return true
        }
}

object ToDoValidator : Validator<UnvalidatedTodo, TodoValidatorMessage, String?>() {
        override fun validate(data: UnvalidatedTodo, metadata: String?): List<TodoValidatorMessage> {
                val msgs = mutableListOf<TodoValidatorMessage>()

                if (data.text.length < 5) {
                        msgs += TodoValidatorMessage("Text length must be at least 5 characters")
                }
                if (data.text.contains("'")) {
                        msgs += TodoValidatorMessage("Text cannot contain the ' character")
                }

                return msgs
        }
}