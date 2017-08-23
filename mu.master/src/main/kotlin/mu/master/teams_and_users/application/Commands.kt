package mu.master.teams_and_users.application

import mu.libs.cqrs.ICommand
import mu.master.teams_and_users.domain.TeamId
import mu.master.teams_and_users.domain.UserId


data class CreateTeam(
        val teamId: TeamId,
        val teamAdmin: UserId,
        val displayName: String,
        val byUser: UserId) : ICommand

data class AddUserToTeam(
        val team: TeamId,
        val user: UserId,
        val byUser: UserId) : ICommand

data class RemoveUserFromTeam(
        val team: TeamId,
        val user: UserId,
        val byUser: UserId) : ICommand
