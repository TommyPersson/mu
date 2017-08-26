package mu.libs.cqrs

import mu.libs.utils.getLogger

class CommandHandlersBuilder {
    val handlers = mutableMapOf<Class<*>, ICommandHandler<*>>()

    inline fun <reified T : ICommand> handlerOf(crossinline handler: (T) -> Unit) {
        handlers.put(T::class.java, object : ICommandHandler<T> {
            override fun handle(command: T) {
                handler(command)
            }
        })
    }

    fun build(): ICommandHandler<ICommand> {
        return GenericCommandHandler(handlers)
    }

    private class GenericCommandHandler(
            private val handlers: Map<Class<*>, ICommandHandler<*>>
    ) : ICommandHandler<ICommand> {
        private val logger = getLogger()

        @Suppress("UNCHECKED_CAST")
        override fun handle(command: ICommand) {
            logger.info("Handling command: $command")
            val commandHandler = handlers[command.javaClass]!! as ICommandHandler<ICommand>
            commandHandler.handle(command)
        }
    }
}

fun createCommandHandlers(handlerCreator: CommandHandlersBuilder.() -> Unit): ICommandHandler<ICommand> {
    val builder = CommandHandlersBuilder()
    handlerCreator(builder)
    return builder.build()
}