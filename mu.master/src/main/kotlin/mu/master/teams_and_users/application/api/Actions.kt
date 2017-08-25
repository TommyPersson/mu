package mu.master.teams_and_users.application.api

import mu.master.ApiAction
import mu.master.DI
import mu.master.teams_and_users.application.CreateTeam
import mu.master.teams_and_users.application.teamsCommandHandlers
import mu.master.teams_and_users.domain.TeamId
import mu.master.teams_and_users.domain.UserId
import mu.master.teams_and_users.views.ITeamsView

object TeamsAndUsersApiActions {
    val all = listOf(
            CreateTeamAction(),
            ListTeamsAction(DI.TeamsAndUsers.teamsView))
}

class CreateTeamAction : ApiAction<CreateTeamRequestDTO, CreateTeamResponseDTO>("teams.create", CreateTeamRequestDTO::class) {
    suspend override fun invoke(userId: UserId, input: CreateTeamRequestDTO): CreateTeamResponseDTO {
        println("creating team ${input.name}")

        val newTeamId = TeamId.create()

        teamsCommandHandlers.handle(CreateTeam(
                teamId = newTeamId,
                teamAdmin = UserId(input.teamAdminUserId),
                displayName = input.name,
                byUser = userId))

        return CreateTeamResponseDTO(teamId = newTeamId.value)
    }
}

class ListTeamsAction(
        private val teamsView: ITeamsView
) : ApiAction<EmptyRequestDTO, ListTeamsResponseDTO>("teams.list", EmptyRequestDTO::class) {

    suspend override fun invoke(userId: UserId, input: EmptyRequestDTO): ListTeamsResponseDTO {
        val teams = teamsView.teams

        return ListTeamsResponseDTO(teams)
    }
}
