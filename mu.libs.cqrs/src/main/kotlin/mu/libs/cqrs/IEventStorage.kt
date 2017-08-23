package mu.libs.cqrs

interface IEventStorage {
    fun getEventsForAggregate(aggregateRootId: AggregateRootId): List<EventEnvelope>

    fun getAllEvents(): List<EventEnvelope>

    fun add(eventEnvelope: EventEnvelope)
}