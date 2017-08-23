package mu.master.teams_and_users.domain

import mu.libs.cqrs.IEvent


data class UserRegistered(
        val userId: UserId,
        val userName: String,
        val email: String,
        val displayName: String) : IEvent

data class TeamCreated(
        val teamId: TeamId,
        val teamAdmin: UserId,
        val displayName: String,
        val byUser: UserId) : IEvent

data class UserAddedToTeam(
        val team: TeamId,
        val user: UserId,
        val byUser: UserId) : IEvent

data class UserRemovedFromTeam(
        val team: TeamId,
        val user: UserId,
        val byUser: UserId) : IEvent
