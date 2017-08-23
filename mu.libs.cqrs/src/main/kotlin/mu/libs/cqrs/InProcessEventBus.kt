package mu.libs.cqrs

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class InProcessEventBus : IEventBus {
    // TODO thread-safety
    private val subscribers = mutableMapOf<Class<*>, MutableSet<BehaviorSubject<IEvent>>>()

    override fun <T : IEvent> publish(event: T) {
        subscribers[event.javaClass]?.forEach { it.onNext(event) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : IEvent> subscribe(eventType: Class<T>): Observable<T> {
        val subject = BehaviorSubject.create<IEvent>()

        val subjects = subscribers.getOrPut(eventType, { mutableSetOf() })

        subject.doOnDispose {
            subjects.remove(subject)
        }

        subjects.add(subject)

        return subject as Observable<T>
    }
}