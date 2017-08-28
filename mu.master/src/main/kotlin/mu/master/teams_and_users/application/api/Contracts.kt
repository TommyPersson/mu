package mu.master.teams_and_users.application.api

import java.util.*

class EmptyRequestDTO
class EmptyResponseDTO

data class ListTeamsResponseDTO(
        val teams: List<TeamDTO>)

data class CreateTeamRequestDTO(
        val teamAdminUserId: UUID,
        val name: String)

data class CreateTeamResponseDTO(
        val teamId: UUID)

data class AddUserToTeamRequestDTO(
        val userId: UUID,
        val teamId: UUID)

data class RemoveUserFromTeamRequestDTO(
        val userId: UUID,
        val teamId: UUID)

data class UserDTO(
        val id: UUID)

data class TeamDTO(
        val id: UUID,
        val name: String,
        val teamAdminId: UUID,
        val memberIds: Set<UUID>)