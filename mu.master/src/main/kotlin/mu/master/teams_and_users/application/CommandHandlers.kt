package mu.master.teams_and_users.application

import mu.libs.cqrs.ICommand
import mu.libs.cqrs.ICommandHandler
import mu.master.DI
import mu.master.teams_and_users.domain.ITeamRepository
import mu.master.teams_and_users.domain.Team

object CommandHandlers {
    private val handlers = mapOf<Class<*>, ICommandHandler<*>>(
            CreateTeam::class.java to CreateTeamCommandHandler(DI.TeamsAndUsers.teamRepository),
            AddUserToTeam::class.java to AddUserToTeamCommandHandler(DI.TeamsAndUsers.teamRepository),
            RemoveUserFromTeam::class.java to RemoveUserFromTeamCommandHandler(DI.TeamsAndUsers.teamRepository)
    )

    @Suppress("UNCHECKED_CAST")
    fun handle(command: ICommand) {
        val commandHandler = handlers[command.javaClass]!! as ICommandHandler<ICommand>
        commandHandler.handle(command)
    }
}

class CreateTeamCommandHandler(
        private val teamRepository: ITeamRepository
) : ICommandHandler<CreateTeam> {

    override fun handle(command: CreateTeam) {
        val team = Team(command.teamId, command.displayName, command.teamAdmin, command.byUser)
        teamRepository.save(team)
    }
}

class AddUserToTeamCommandHandler(
        private val teamRepository: ITeamRepository
) : ICommandHandler<AddUserToTeam> {

    override fun handle(command: AddUserToTeam) {
        val team = teamRepository.getById(command.team.value) ?: return
        team.addUser(command.user, command.byUser)
        teamRepository.save(team)
    }
}

class RemoveUserFromTeamCommandHandler(
        private val teamRepository: ITeamRepository
) : ICommandHandler<RemoveUserFromTeam> {

    override fun handle(command: RemoveUserFromTeam) {
        val team = teamRepository.getById(command.team.value) ?: return
        team.removeUser(command.user, command.byUser)
        teamRepository.save(team)
    }
}
