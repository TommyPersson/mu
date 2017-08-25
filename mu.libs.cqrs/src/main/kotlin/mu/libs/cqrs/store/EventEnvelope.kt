package mu.libs.cqrs.store

import mu.libs.cqrs.IEvent
import java.util.*

data class EventEnvelope(
        val aggregateRootId: UUID,
        val eventData: IEvent,
        val eventType: String,
        val version: Int)