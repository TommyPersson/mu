package mu.libs.cqrs


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
        return object : ICommandHandler<ICommand> {
            @Suppress("UNCHECKED_CAST")
            override fun handle(command: ICommand) {
                val commandHandler = handlers[command.javaClass]!! as ICommandHandler<ICommand>
                commandHandler.handle(command)
            }
        }
    }
}

fun createCommandHandlers(handlerCreator: CommandHandlersBuilder.() -> Unit): ICommandHandler<ICommand> {
    val builder = CommandHandlersBuilder()
    handlerCreator(builder)
    return builder.build()
}