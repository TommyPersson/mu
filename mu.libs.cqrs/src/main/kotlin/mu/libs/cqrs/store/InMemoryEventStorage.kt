package mu.libs.cqrs.store

import mu.libs.cqrs.AggregateRootId
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

    override fun transact(aggregateRootId: AggregateRootId, transactor: IEventStorage.IEventTransaction.(Int) -> Unit) {
        val events = store.getOrPut(aggregateRootId, { mutableListOf() })

        synchronized(events) {
            val version = events.lastOrNull()?.version ?: 0

            val transaction = object : IEventStorage.IEventTransaction {
                override fun appendToLog(eventEnvelope: EventEnvelope) {
                    store.getOrPut(AggregateRootId(eventEnvelope.aggregateRootId), { mutableListOf() }).add(eventEnvelope)
                    allEvents.add(eventEnvelope)
                }
            }

            transactor(transaction, version)
        }
    }
}