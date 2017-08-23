package mu.libs.cqrs

import java.util.*

open class AbstractRepository<TAggregate : AggregateRoot>(
        private val eventStore: IEventStore,
        private val aggregateFactory: () -> TAggregate
): IRepository<TAggregate> {

    override fun getById(id: UUID): TAggregate? {
        val eventStream = eventStore.getEventHistory(AggregateRootId(id))

        return aggregateFactory().apply {
            loadFromHistory(eventStream.events.asSequence(), eventStream.version)
        }
    }

    override fun save(aggregate: TAggregate) {
        eventStore.saveEvents(aggregate.id, aggregate.uncommittedChanges, aggregate.version)
        aggregate.markAllChangesAsCommitted()
    }
}