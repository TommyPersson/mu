package mu.libs.cqrs

abstract class AllConsumingView(
        private val eventStore: IEventStore
) : IView {
    private var lastSequence = SequenceNumber(0)

    override fun initialize() {
        eventStore.subscribeForEventAdditions().subscribe {
            val nextSequence = SequenceNumber(lastSequence.value + 1)
            eventStore.replayAllEvents(nextSequence, 200).subscribe {
                synchronized(this) {
                    val event = it.first
                    val sequence = it.second

                    if (lastSequence == SequenceNumber(sequence.value - 1)) {
                        handle(event)
                        lastSequence = sequence
                    }
                }
            }
        }

        // TODO: load all existing events
    }

    protected abstract fun handle(event: IEvent)
}