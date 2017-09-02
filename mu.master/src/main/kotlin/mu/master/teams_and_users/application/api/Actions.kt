package mu.master.teams_and_users.application.api

import mu.master.DI
import mu.master.teams_and_users.application.AddUserToTeam
import mu.master.teams_and_users.application.CreateTeam
import mu.master.teams_and_users.application.RemoveUserFromTeam
import mu.master.teams_and_users.application.teamsCommandHandlers
import mu.master.teams_and_users.domain.TeamId
import mu.master.teams_and_users.domain.UserId
import mu.master.utils.createActions

val teamsAndUsersContextActions = createActions {

    val teamsView = DI.TeamsAndUsers.teamsView

    actionOf<EmptyRequestDTO, ListTeamsResponseDTO>("teams.list") { _, _ ->
        val teams = teamsView.teams

        ListTeamsResponseDTO(teams)
    }

    actionOf<CreateTeamRequestDTO, CreateTeamResponseDTO>("teams.create") { input, context ->
        val newTeamId = TeamId.new()
        val currentUserId = UserId(context.userPrincipal!!.userId)

        teamsCommandHandlers.handle(CreateTeam(
                teamId = newTeamId,
                teamAdmin = UserId(input.teamAdminUserId),
                displayName = input.name,
                byUser = currentUserId))

        CreateTeamResponseDTO(teamId = newTeamId.value)
    }

    actionOf<AddUserToTeamRequestDTO, EmptyResponseDTO>("teams.add-user") { input, context ->
        val teamId = TeamId(input.teamId)
        val userId = UserId(input.userId)
        val currentUserId = UserId(context.userPrincipal!!.userId)

        teamsCommandHandlers.handle(AddUserToTeam(
                team = teamId,
                user = userId,
                byUser = currentUserId))

        EmptyResponseDTO()
    }

    actionOf<AddUserToTeamRequestDTO, EmptyResponseDTO>("teams.remove-user") { input, context ->
        val teamId = TeamId(input.teamId)
        val userId = UserId(input.userId)
        val currentUserId = UserId(context.userPrincipal!!.userId)

        teamsCommandHandlers.handle(RemoveUserFromTeam(
                team = teamId,
                user = userId,
                byUser = currentUserId))

        EmptyResponseDTO()
    }

}