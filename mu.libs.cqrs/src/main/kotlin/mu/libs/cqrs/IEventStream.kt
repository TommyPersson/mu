package mu.libs.cqrs

interface IEventStream {
    val events: List<IEvent>
    val version: Int
}