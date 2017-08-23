package mu.libs.cqrs

import io.reactivex.Observable


interface IEventStore {
    fun saveEvents(aggregateRootId: AggregateRootId, events: List<IEvent>, expectedVersion: Int)

    fun getEventHistory(aggregateRootId: AggregateRootId): IEventStream

    fun replayAllEvents(afterSequence: SequenceNumber, limit: Int): Observable<Pair<IEvent, SequenceNumber>>

    fun subscribeForEventAdditions(): Observable<SequenceNumber>
}