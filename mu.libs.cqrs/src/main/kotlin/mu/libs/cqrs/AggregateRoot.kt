package mu.libs.cqrs

abstract class AggregateRoot {

    private val _generated = mutableListOf<IEvent>()

    abstract val id: AggregateRootId

    val uncommittedChanges: List<IEvent> = _generated
    var version: Int = 0

    fun markAllChangesAsCommitted() {
        _generated.clear()
    }

    fun loadFromHistory(history: Sequence<IEvent>, version: Int) {
        for (event in history) {
            applyChange(event, false)
        }

        this.version = version
    }

    protected abstract fun apply(event: IEvent): Unit

    protected fun applyChange(event: IEvent, isNew: Boolean = true) {
        apply(event)

        if (isNew) {
            _generated.add(event)
        }
    }
}