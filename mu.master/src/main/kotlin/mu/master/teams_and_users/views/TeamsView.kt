package mu.master.teams_and_users.views

import mu.libs.cqrs.*
import mu.master.teams_and_users.application.api.TeamDTO
import mu.master.teams_and_users.domain.TeamCreated
import mu.master.teams_and_users.domain.UserAddedToTeam
import mu.master.teams_and_users.domain.UserRemovedFromTeam

class TeamsView(eventStore: IEventStore) : ITeamsView, AllConsumingView(eventStore) {

    private val _teams = mutableListOf<TeamDTO>()
    override val teams: List<TeamDTO> = _teams

    override fun handle(event: IEvent) {
        when (event) {
            is TeamCreated -> {
                _teams.add(TeamDTO(
                        id = event.teamId.value,
                        name = event.displayName,
                        teamAdminId = event.teamAdmin.value,
                        memberIds = setOf(event.teamAdmin.value, event.byUser.value)
                ))
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