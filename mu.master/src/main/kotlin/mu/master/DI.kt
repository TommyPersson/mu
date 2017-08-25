package mu.master

import mu.libs.cqrs.store.EventStore
import mu.libs.cqrs.store.IEventStore
import mu.master.teams_and_users.domain.ITeamRepository
import mu.master.teams_and_users.infrastructure.TeamRepository
import mu.master.teams_and_users.views.ITeamsView
import mu.master.teams_and_users.views.TeamsView
import java.io.File

object DI { // TODO Checkout https://salomonbrys.github.io/Kodein/?
    object Common {
        val eventStore: IEventStore = EventStore.createJsonFileEventStore(File(System.getProperty("user.home"), ".mu/master/event-store.json"))
    }

    object TeamsAndUsers {
        val teamsView: ITeamsView = TeamsView(DI.Common.eventStore)
        val teamRepository: ITeamRepository = TeamRepository(DI.Common.eventStore)
    }
}