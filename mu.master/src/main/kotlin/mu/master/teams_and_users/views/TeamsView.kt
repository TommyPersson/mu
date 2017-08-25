package mu.master.teams_and_users.views

import mu.libs.cqrs.*
import mu.libs.cqrs.store.IEventStore
import mu.master.teams_and_users.application.api.TeamDTO
import mu.master.teams_and_users.application.api.UserDTO
import mu.master.teams_and_users.domain.TeamCreated
import mu.master.teams_and_users.domain.TeamId
import mu.master.teams_and_users.domain.UserAddedToTeam
import mu.master.teams_and_users.domain.UserRemovedFromTeam

class TeamsView(eventStore: IEventStore) : ITeamsView, AllConsumingView(eventStore) {

    private val _teams = mutableListOf<TeamDTO>()
    override val teams: List<TeamDTO> = _teams

    private val _usersByTeam = mutableMapOf<TeamId, MutableSet<UserDTO>>()
    override val usersByTeam: Map<TeamId, Set<UserDTO>> = _usersByTeam

    override fun handle(event: IEvent) {
        when (event) {
            is TeamCreated -> {
                _teams.add(TeamDTO(
                        id = event.teamId.value,
                        name = event.displayName,
                        teamAdminId = event.teamAdmin.value,
                        memberIds = setOf(event.teamAdmin.value, event.byUser.value)
                ))

                // just get some data in for experiments
                _usersByTeam.getOrPut(event.teamId) { mutableSetOf() }.addAll(
                        listOf(UserDTO(event.teamAdmin.value), UserDTO(event.byUser.value)))
            }

            is UserAddedToTeam -> {
                TODO()
            }

            is UserRemovedFromTeam -> {
                TODO()
            }
        }
    }

}