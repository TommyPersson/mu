package mu.libs.cqrs.store

import mu.libs.cqrs.IEvent

class EventStream(
        override val events: List<IEvent>,
        override val version: Int
) : IEventStream