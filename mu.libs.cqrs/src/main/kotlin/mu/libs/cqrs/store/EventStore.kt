package mu.libs.cqrs.store

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mu.libs.cqrs.AggregateRootId
import mu.libs.cqrs.IEvent
import java.io.File


class EventStore(
        private val eventStorage: IEventStorage
) : IEventStore {

    companion object {
        fun createInMemoryEventStore() = EventStore(InMemoryEventStorage())

        fun createJsonFileEventStore(file: File) = EventStore(JsonFileEventStorage(file))
    }

    private val eventAdditionSubject = BehaviorSubject.createDefault(SequenceNumber(0))

    override fun saveEvents(aggregateRootId: AggregateRootId, events: List<IEvent>, expectedVersion: Int) {
        eventStorage.transact(aggregateRootId) { version ->

            if (version != expectedVersion) {
                throw OptimisticConcurrencyException()
            }

            for ((index, event) in events.withIndex()) {
                val nextVersion = expectedVersion + index + 1

                val envelope = EventEnvelope(
                        aggregateRootId = aggregateRootId.id,
                        eventData = event,
                        eventType = event.javaClass.canonicalName,
                        version = nextVersion)

                appendToLog(envelope)
                eventAdditionSubject.onNext(SequenceNumber(eventStorage.getAllEvents().size))
            }
        }
    }

    override fun getEventHistory(aggregateRootId: AggregateRootId): IEventStream? {
        val eventEnvelopes = eventStorage.getEventsForAggregate(aggregateRootId) ?: return null
        val events = eventEnvelopes.map { it.eventData }

        return EventStream(
                events = events,
                version = eventEnvelopes.lastOrNull()?.version ?: 0
        )
    }

    override fun replayAllEvents(afterSequence: SequenceNumber, limit: Int): Observable<Pair<IEvent, SequenceNumber>> {
        return Observable.create {
            val allEvents = eventStorage.getAllEvents()
            val seq = afterSequence.value
            for (i in seq..(seq + limit)) {
                val envelope = allEvents.getOrNull(i - 1) ?: break
                it.onNext(envelope.eventData to SequenceNumber(i))
            }
            it.onComplete()
        }
    }

    override fun subscribeForEventAdditions(): Observable<SequenceNumber> {
        return eventAdditionSubject
    }
}

