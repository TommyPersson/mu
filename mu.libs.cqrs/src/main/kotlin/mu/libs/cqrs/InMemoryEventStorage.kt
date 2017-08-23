package mu.libs.cqrs

import java.util.*

class InMemoryEventStorage : IEventStorage {
    private val store = mutableMapOf<AggregateRootId, MutableList<EventEnvelope>>()
    private val allEvents = Collections.synchronizedList(mutableListOf<EventEnvelope>())

    override fun getEventsForAggregate(aggregateRootId: AggregateRootId): List<EventEnvelope> {
        return store[aggregateRootId] ?: emptyList()
    }

    override fun getAllEvents(): List<EventEnvelope> {
        return allEvents
    }

    override fun add(eventEnvelope: EventEnvelope) {
        store.getOrPut(AggregateRootId(eventEnvelope.aggregateRootId), { mutableListOf() }).add(eventEnvelope)
        allEvents.add(eventEnvelope)
    }
}