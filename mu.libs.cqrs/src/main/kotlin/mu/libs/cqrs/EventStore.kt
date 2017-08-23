package mu.libs.cqrs

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

val inMemoryEventStore = EventStore(InMemoryEventStorage())

class EventStore(
        private val eventStorage: IEventStorage
) : IEventStore {

    private val eventAdditionSubject = BehaviorSubject.createDefault(SequenceNumber(0))

    override fun saveEvents(aggregateRootId: AggregateRootId, events: List<IEvent>, expectedVersion: Int) {
        val storedEvents = eventStorage.getEventsForAggregate(aggregateRootId)

        synchronized(storedEvents) {
            val lastStoredVersion = storedEvents.lastOrNull()?.version ?: 0
            if (lastStoredVersion != expectedVersion) {
                throw OptimisticConcurrencyException()
            }

            for ((index, event) in events.withIndex()) {
                val nextVersion = expectedVersion + index + 1

                val envelope = EventEnvelope(
                        aggregateRootId = aggregateRootId.id,
                        eventData = event,
                        eventType = event.javaClass.simpleName,
                        version = nextVersion)

                eventStorage.add(envelope)
                eventAdditionSubject.onNext(SequenceNumber(eventStorage.getAllEvents().size))
            }
        }
    }

    override fun getEventHistory(aggregateRootId: AggregateRootId): IEventStream {
        val eventEnvelopes = eventStorage.getEventsForAggregate(aggregateRootId)
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

