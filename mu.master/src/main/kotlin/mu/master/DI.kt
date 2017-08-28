package mu.master

import mu.libs.cqrs.store.EventStore
import mu.libs.cqrs.store.IEventStore
import mu.master.identity.application.services.IIdentityService
import mu.master.identity.application.services.IdentityService
import mu.master.identity.domain.IUserAccountRepository
import mu.master.identity.domain.services.IEmailAvailabilityChecker
import mu.master.identity.domain.services.IPasswordHasher
import mu.master.identity.infrastructure.EmailAvailabilityChecker
import mu.master.identity.infrastructure.PasswordHasher
import mu.master.identity.infrastructure.UserAccountRepository
import mu.master.identity.views.IIdentityView
import mu.master.identity.views.IdentityView
import mu.master.teams_and_users.domain.ITeamRepository
import mu.master.teams_and_users.domain.services.IUserExistenceChecker
import mu.master.teams_and_users.infrastructure.TeamRepository
import mu.master.teams_and_users.infrastructure.UserExistenceChecker
import mu.master.teams_and_users.views.ITeamsView
import mu.master.teams_and_users.views.TeamsView
import java.io.File

object DI { // TODO Checkout https://salomonbrys.github.io/Kodein/?
    object Common {

    }

    object Identity {
        private val eventStore: IEventStore = EventStore.createJsonFileEventStore(File(System.getProperty("user.home"), ".mu/master/identity/event-store.json"))
        val identityView: IIdentityView = IdentityView(eventStore)
        val userAccountRepository: IUserAccountRepository = UserAccountRepository(eventStore)
        val passwordHasher: IPasswordHasher = PasswordHasher()
        val emailAvailabilityChecker: IEmailAvailabilityChecker = EmailAvailabilityChecker(identityView)
        val identityService: IIdentityService = IdentityService(DI.Identity.identityView)
    }

    object TeamsAndUsers {
        private val eventStore: IEventStore = EventStore.createJsonFileEventStore(File(System.getProperty("user.home"), ".mu/master/teams-and-users/event-store.json"))
        val userExistenceChecker: IUserExistenceChecker = UserExistenceChecker(DI.Identity.identityService)
        val teamsView: ITeamsView = TeamsView(eventStore)
        val teamRepository: ITeamRepository = TeamRepository(eventStore)
    }
}