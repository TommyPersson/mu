package mu.libs.cqrs

import mu.libs.cqrs.store.IEventStore
import java.util.*

open class AbstractRepository<TAggregate : AggregateRoot>(
        private val eventStore: IEventStore,
        private val aggregateFactory: () -> TAggregate
): IRepository<TAggregate> {

    override fun getById(id: UUID): TAggregate? {
        val eventStream = eventStore.getEventHistory(AggregateRootId(id)) ?: return null

        return aggregateFactory().apply {
            loadFromHistory(eventStream.events.asSequence(), eventStream.version)
        }
    }

    override fun save(aggregate: TAggregate) {
        eventStore.saveEvents(aggregate.id, aggregate.uncommittedChanges, aggregate.version)
        aggregate.markAllChangesAsCommitted()
    }

    override fun update(id: UUID, updateFn: (TAggregate) -> Unit) {
        val aggregate = getById(id) ?: return
        updateFn(aggregate)
        save(aggregate)
    }
}