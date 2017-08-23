package mu.libs.cqrs

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import java.util.*

val inMemoryEventStore = InMemoryEventStore()

class InMemoryEventStore : IEventStore {

    data class EventEnvelope(
            val aggregateRootId: UUID,
            val eventData: IEvent,
            val eventType: String,
            val version: Int)

    private val store = mutableMapOf<AggregateRootId, MutableList<EventEnvelope>>()
    private val allEvents = Collections.synchronizedList(mutableListOf<EventEnvelope>())
    private val eventAdditionSubject = BehaviorSubject.createDefault(SequenceNumber(0))

    override fun saveEvents(aggregateRootId: AggregateRootId, events: List<IEvent>, expectedVersion: Int) {
        val storedEvents = getStoredEvents(aggregateRootId)

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

                storedEvents.add(envelope)
                allEvents.add(envelope)
                eventAdditionSubject.onNext(SequenceNumber(allEvents.size))
            }
        }
    }

    override fun getEventHistory(aggregateRootId: AggregateRootId): IEventStream {
        val eventEnvelopes = getStoredEvents(aggregateRootId)
        val events = eventEnvelopes.map { it.eventData }

        return EventStream(
                events = events,
                version = eventEnvelopes.lastOrNull()?.version ?: 0
        )
    }

    override fun replayAllEvents(afterSequence: SequenceNumber, limit: Int): Observable<Pair<IEvent, SequenceNumber>> {
        return Observable.create {
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

    private fun getStoredEvents(aggregateRootId: AggregateRootId): MutableList<EventEnvelope> {
        return store.getOrPut(aggregateRootId, { mutableListOf() })
    }
}

