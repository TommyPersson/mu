package mu.master.teams_and_users.views

import mu.libs.cqrs.IView
import mu.master.teams_and_users.application.api.TeamDTO
import java.util.*

interface ITeamsView : IView {
    val teams: List<TeamDTO>
    val teamsById: Map<UUID, TeamDTO>
}