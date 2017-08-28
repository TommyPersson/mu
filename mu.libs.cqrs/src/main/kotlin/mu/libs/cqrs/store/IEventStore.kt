package mu.libs.cqrs.store

import io.reactivex.Observable
import mu.libs.cqrs.AggregateRootId
import mu.libs.cqrs.IEvent


interface IEventStore {
    fun saveEvents(aggregateRootId: AggregateRootId, events: List<IEvent>, expectedVersion: Int)

    fun getEventHistory(aggregateRootId: AggregateRootId): IEventStream?

    fun replayAllEvents(afterSequence: SequenceNumber, limit: Int): Observable<Pair<IEvent, SequenceNumber>>

    fun subscribeForEventAdditions(): Observable<SequenceNumber>
}