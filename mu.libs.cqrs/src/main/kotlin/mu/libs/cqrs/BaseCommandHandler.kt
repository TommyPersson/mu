package mu.libs.cqrs

import mu.libs.cqrs.store.IEventStore

abstract class BaseCommandHandler<T : ICommand>(private val eventStore: IEventStore): ICommandHandler<T> {
    final override fun handle(command: T) {
    }

    protected abstract fun doHandle(command: T): AggregateRoot

}