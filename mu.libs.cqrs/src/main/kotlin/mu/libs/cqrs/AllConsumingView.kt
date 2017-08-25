package mu.libs.cqrs

import mu.libs.cqrs.store.IEventStore
import mu.libs.cqrs.store.SequenceNumber

abstract class AllConsumingView(
        private val eventStore: IEventStore
) : IView {
    private var lastSequence = SequenceNumber(0)

    override fun initialize() {
        eventStore.subscribeForEventAdditions().subscribe {
            fetchNewEvents()
        }

        fetchNewEvents()
    }

    private fun fetchNewEvents() {
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

    protected abstract fun handle(event: IEvent)
}