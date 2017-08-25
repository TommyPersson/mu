package mu.libs.cqrs.store

import mu.libs.cqrs.AggregateRootId

interface IEventStorage {
    fun transact(aggregateRootId: AggregateRootId, transactor: IEventTransaction.(Int) -> Unit)

    fun getEventsForAggregate(aggregateRootId: AggregateRootId): List<EventEnvelope>

    fun getAllEvents(): List<EventEnvelope>

    interface IEventTransaction {
        fun appendToLog(eventEnvelope: EventEnvelope)
    }
}