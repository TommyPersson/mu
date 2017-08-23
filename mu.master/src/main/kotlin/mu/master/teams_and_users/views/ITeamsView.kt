package mu.master.teams_and_users.views

import mu.libs.cqrs.IView
import mu.master.teams_and_users.application.api.TeamDTO

interface ITeamsView : IView {
    val teams: List<TeamDTO>
}