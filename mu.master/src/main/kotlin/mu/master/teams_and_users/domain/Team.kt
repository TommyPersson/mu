package mu.master.teams_and_users.domain

import mu.libs.cqrs.AggregateRoot
import mu.libs.cqrs.AggregateRootId
import mu.libs.cqrs.IEvent
import mu.libs.utils.getLogger

class Team() : AggregateRoot() {

    private val logger = getLogger()

    private lateinit var _id: TeamId

    private lateinit var teamAdmin: UserId
    private val teamMembers = mutableSetOf<UserId>()

    override val id: AggregateRootId get() = AggregateRootId(_id.value)

    constructor(teamId: TeamId, displayName: String, teamAdmin: UserId, createdBy: UserId): this() {
        // TODO: who can create teams? SiteAdmin?
        applyChange(TeamCreated(teamId, teamAdmin, displayName, createdBy))
    }

    fun addUser(userId: UserId, byUser: UserId) {
        // TODO: where to verify that the users exist? here (domain), or command handler (application)?
        if (byUser != teamAdmin) {
            throw IllegalArgumentException("User <$byUser> is not allowed to add members to team <$_id>")
        }

        if (teamMembers.contains(userId)) {
            throw IllegalArgumentException("User <$userId> is already a member of team <$_id>")
        }

        applyChange(UserAddedToTeam(team = _id, user = userId, byUser = byUser))
    }

    fun removeUser(userId: UserId, byUser: UserId) {
        if (byUser != teamAdmin) {
            throw IllegalArgumentException("User <$byUser> is not allowed to add members to team <$_id>")
        }

        if (!teamMembers.contains(userId)) {
            throw IllegalArgumentException("User <$userId> is not a member of team <$_id>")
        }

        if (userId == teamAdmin) {
            throw IllegalArgumentException("Cannot remove the team admin from the team")
        }

        applyChange(UserRemovedFromTeam(team = _id, user = userId, byUser = byUser))
    }

    override fun apply(event: IEvent) {
        when (event) {
            is TeamCreated -> {
                _id = event.teamId
                teamAdmin = event.teamAdmin
                teamMembers.addAll(setOf(event.teamAdmin, event.byUser))
            }
            is UserAddedToTeam -> {
                teamMembers.add(event.user)
            }
            is UserRemovedFromTeam -> {
                teamMembers.remove(event.user)
            }
            else -> {
                logger.error("Unknown event: $event")
            }
        }
    }
}