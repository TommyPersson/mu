package mu.master.teams_and_users.application.api

import java.util.*


data class CreateTeamRequestDTO(
        val teamAdminUserId: UUID,
        val name: String)

data class CreateTeamResponseDTO(
        val teamId: UUID)

data class EmptyRequestDTO(
        val teamAdminUserId: UUID,
        val name: String)


data class ListTeamsResponseDTO(
        val teams: List<TeamDTO>)

data class TeamDTO(
        val id: UUID,
        val name: String,
        val teamAdminId: UUID,
        val memberIds: Set<UUID>)