package mu.master

import mu.libs.cqrs.IEventStore
import mu.libs.cqrs.inMemoryEventStore
import mu.master.teams_and_users.domain.ITeamRepository
import mu.master.teams_and_users.infrastructure.TeamRepository
import mu.master.teams_and_users.views.ITeamsView
import mu.master.teams_and_users.views.TeamsView

object DI { // TODO Checkout https://salomonbrys.github.io/Kodein/?
    object Common {
        val eventStore: IEventStore = inMemoryEventStore
    }

    object TeamsAndUsers {
        val teamsView: ITeamsView = TeamsView(DI.Common.eventStore)
        val teamRepository: ITeamRepository = TeamRepository()
    }
}