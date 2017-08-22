package mu.master.teams_and_users


data class UserRegistered(
        val userId: UserId,
        val userName: String,
        val email: String,
        val displayName: String)

data class TeamCreated(
        val teamId: TeamId,
        val byUser: UserId,
        val teamAdmin: UserId,
        val displayName: String)

data class UserAssignedToTeam(
        val byUser: UserId,
        val user: UserId,
        val team: TeamId)

data class UserRemovedFromTeam(
        val byUser: UserId,
        val user: UserId,
        val team: TeamId)
