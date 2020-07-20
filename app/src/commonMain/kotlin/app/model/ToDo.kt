package app.model

import com.soywiz.klock.annotations.KlockExperimental
import com.soywiz.klock.wrapped.WDateTime
import dev.fritz2.lenses.Lenses
import kotlinx.serialization.*

@Lenses
@Serializable
data class ToDo(
        val id: String = "-1",
        val text: String = "",
        val status: ToDoStatus = Uncompleted
)

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
