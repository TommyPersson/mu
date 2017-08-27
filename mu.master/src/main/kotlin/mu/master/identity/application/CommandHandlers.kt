package mu.master.identity.application

import mu.libs.cqrs.createCommandHandlers
import mu.master.DI
import mu.master.identity.domain.UserAccount

val identityContextCommandHandlers = createCommandHandlers {

    val userAccountService = DI.Identity.emailAvailabilityChecker
    val passwordHasher = DI.Identity.passwordHasher

    handlerOf<RegisterUserAccountCommand> {
        val userAccountRepository = DI.Identity.userAccountRepository
        val userAccount = UserAccount(it.userId, it.displayName, it.email, it.password, userAccountService, passwordHasher)
        userAccountRepository.save(userAccount)
    }

}