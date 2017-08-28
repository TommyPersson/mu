package mu.master.teams_and_users.infrastructure

import mu.master.identity.application.services.IIdentityService
import mu.master.teams_and_users.domain.UserId
import mu.master.teams_and_users.domain.services.IUserExistenceChecker

class UserExistenceChecker(
        private val identityService: IIdentityService
) : IUserExistenceChecker {
    override fun doesUserExist(userId: UserId): Boolean {
        return identityService.doesUserExist(mu.master.identity.domain.UserId(userId.value))
    }
}