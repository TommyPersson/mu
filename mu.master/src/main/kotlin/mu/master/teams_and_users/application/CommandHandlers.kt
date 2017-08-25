package mu.master.teams_and_users.application

import mu.libs.cqrs.createCommandHandlers
import mu.master.DI
import mu.master.teams_and_users.domain.Team

val teamsCommandHandlers = createCommandHandlers {

    val teamRepository = DI.TeamsAndUsers.teamRepository

    handlerOf<CreateTeam> {
        val team = Team(it.teamId, it.displayName, it.teamAdmin, it.byUser)
        teamRepository.save(team)
    }

    handlerOf<AddUserToTeam> {
        val team = teamRepository.getById(it.team.value) ?: return@handlerOf
        team.addUser(it.user, it.byUser)
        teamRepository.save(team)
    }

    handlerOf<RemoveUserFromTeam> {
        val team = teamRepository.getById(it.team.value) ?: return@handlerOf
        team.removeUser(it.user, it.byUser)
        teamRepository.save(team)
    }
}
