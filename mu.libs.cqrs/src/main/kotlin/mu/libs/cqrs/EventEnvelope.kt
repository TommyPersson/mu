package mu.libs.cqrs

import java.util.*

data class EventEnvelope(
        val aggregateRootId: UUID,
        val eventData: IEvent,
        val eventType: String,
        val version: Int)