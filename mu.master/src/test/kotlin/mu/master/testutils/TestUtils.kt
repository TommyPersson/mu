package mu.master.testutils

import mu.libs.cqrs.AggregateRoot
import mu.libs.cqrs.IEvent
import org.junit.jupiter.api.Assertions


infix fun AggregateRoot.hasPublished(event: IEvent) {
    Assertions.assertEquals(event, this.uncommittedChanges.single())
}

infix fun AggregateRoot.hasPublished(events: List<IEvent>) {
    Assertions.assertEquals(events, this.uncommittedChanges)
}

fun <T : AggregateRoot> (() -> T).from(vararg events: IEvent): T {
    return this().apply { loadFromHistory(events.asSequence(), events.size) }
}

fun <T : AggregateRoot> T.applyChanges(vararg events: IEvent): T {
    return this.apply { loadFromHistory(events.asSequence(), events.size + this.version) }
}

inline fun <reified E : Throwable> (() -> Any).throws() {
    Assertions.assertThrows(E::class.java, { this() })
}