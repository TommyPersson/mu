package mu.master.teams_and_users.views

import mu.libs.cqrs.AllConsumingView
import mu.libs.cqrs.IEvent
import mu.libs.cqrs.store.IEventStore
import mu.master.teams_and_users.application.api.TeamDTO
import mu.master.teams_and_users.domain.TeamCreated
import mu.master.teams_and_users.domain.UserAddedToTeam
import mu.master.teams_and_users.domain.UserRemovedFromTeam
import java.util.*

class TeamsView(eventStore: IEventStore) : ITeamsView, AllConsumingView(eventStore) {

    private val _teams = mutableListOf<TeamDTO>()
    override val teams: List<TeamDTO> = _teams

    private var _teamsById = emptyMap<UUID, TeamDTO>()
    override val teamsById: Map<UUID, TeamDTO> get() = _teamsById

    override fun handle(event: IEvent) {
        when (event) {
            is TeamCreated -> {
                _teams.add(TeamDTO(
                        id = event.teamId.value,
                        name = event.displayName,
                        teamAdminId = event.teamAdmin.value,
                        memberIds = setOf(event.teamAdmin.value, event.byUser.value)))

                refreshIndexes()
            }

            is UserAddedToTeam -> {
                _teams.replaceAll {
                    if (it.id == event.team.value) {
                        it.copy(memberIds = it.memberIds + event.user.value)
                    } else it
                }

                refreshIndexes()
            }

            is UserRemovedFromTeam -> {
                _teams.replaceAll {
                    if (it.id == event.team.value) {
                        it.copy(memberIds = it.memberIds - event.user.value)
                    } else it
                }

                refreshIndexes()
            }
        }
    }

    private fun refreshIndexes() {
        _teamsById = teams.associateBy { it.id }
    }
}