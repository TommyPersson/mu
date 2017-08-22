package mu.master.teams_and_users.api

import java.util.*


data class CreateTeamRequestDTO(
        val teamAdminUserId: UUID,
        val name: String)

data class CreateTeamResponseDTO(
        val teamAdminUserId: UUID,
        val createdByUserId: UUID,
        val teamId: UUID,
        val name: String)