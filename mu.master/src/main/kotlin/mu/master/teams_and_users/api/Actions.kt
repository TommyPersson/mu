package mu.master.teams_and_users.api

import mu.master.ApiAction
import mu.master.teams_and_users.TeamId
import mu.master.teams_and_users.UserId

object TeamsAndUsersApiActions {
    val all = listOf(CreateTeamAction())
}

class CreateTeamAction : ApiAction<CreateTeamRequestDTO, CreateTeamResponseDTO>("teams.create", CreateTeamRequestDTO::class) {

    suspend override fun invoke(userId: UserId, input: CreateTeamRequestDTO): CreateTeamResponseDTO {
        println("creating team ${input.name}")
        val newTeamId = TeamId.create()

        return CreateTeamResponseDTO(
                teamAdminUserId = input.teamAdminUserId,
                createdByUserId = userId.value,
                teamId = newTeamId.value,
                name = input.name
        )
    }
}