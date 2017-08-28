package mu.master.teams_and_users.application

import mu.libs.cqrs.createCommandHandlers
import mu.master.DI
import mu.master.teams_and_users.domain.Team

val teamsCommandHandlers = createCommandHandlers {

    val teamRepository = DI.TeamsAndUsers.teamRepository
    val userExistenceChecker = DI.TeamsAndUsers.userExistenceChecker

    handlerOf<CreateTeam> {
        val team = Team(it.teamId, it.displayName, it.teamAdmin, it.byUser, userExistenceChecker)
        teamRepository.save(team)
    }

    handlerOf<AddUserToTeam> { cmd ->
        teamRepository.update(cmd.team.value) {
            it.addUser(cmd.user, cmd.byUser, userExistenceChecker)
        }
    }

    handlerOf<RemoveUserFromTeam> { cmd ->
        teamRepository.update(cmd.team.value) {
            it.removeUser(cmd.user, cmd.byUser)
        }
    }
}
