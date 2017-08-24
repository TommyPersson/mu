package mu.master.teams_and_users.views

import mu.libs.cqrs.IView
import mu.master.teams_and_users.application.api.TeamDTO
import mu.master.teams_and_users.application.api.UserDTO
import mu.master.teams_and_users.domain.TeamId

interface ITeamsView : IView {
    val teams: List<TeamDTO>
    val usersByTeam: Map<TeamId, Set<UserDTO>>
}