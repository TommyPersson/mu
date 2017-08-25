package mu.libs.cqrs.store

import mu.libs.cqrs.IEvent

interface IEventStream {
    val events: List<IEvent>
    val version: Int
}