package mu.libs.cqrs

import io.reactivex.Observable

interface IEventBus {
    fun <TEvent : IEvent> publish(event: TEvent)

    fun <TEvent : IEvent> subscribe(eventType: Class<TEvent>): Observable<TEvent>
}