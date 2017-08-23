package mu.libs.cqrs

class EventStream(
        override val events: List<IEvent>,
        override val version: Int
) : IEventStream