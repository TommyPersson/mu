package mu.libs.cqrs

interface ICommandHandler<in T : ICommand> {
    fun handle(command: T)
}