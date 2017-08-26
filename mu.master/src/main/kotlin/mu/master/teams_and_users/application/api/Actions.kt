package mu.master.teams_and_users.application.api

import mu.master.DI
import mu.master.teams_and_users.application.CreateTeam
import mu.master.teams_and_users.application.teamsCommandHandlers
import mu.master.teams_and_users.domain.TeamId
import mu.master.teams_and_users.domain.UserId
import mu.master.utils.createActions

val teamsAndUsersApiActions = createActions {

    val teamsView = DI.TeamsAndUsers.teamsView

    actionOf<CreateTeamRequestDTO, CreateTeamResponseDTO>("teams.create") { input, userId ->
        val newTeamId = TeamId.create()

        teamsCommandHandlers.handle(CreateTeam(
                teamId = newTeamId,
                teamAdmin = UserId(input.teamAdminUserId),
                displayName = input.name,
                byUser = userId))

        CreateTeamResponseDTO(teamId = newTeamId.value)
    }

    actionOf<EmptyRequestDTO, ListTeamsResponseDTO>("teams.list") { _, userId ->
        val teams = teamsView.teams

        ListTeamsResponseDTO(teams)
    }

}