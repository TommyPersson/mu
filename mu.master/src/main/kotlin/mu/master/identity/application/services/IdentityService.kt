package mu.master.identity.application.services

import mu.master.identity.domain.AuthToken
import mu.master.identity.domain.PasswordHash
import mu.master.identity.domain.UserId
import mu.master.identity.domain.services.IAuthTokenCreator
import mu.master.identity.domain.services.IPasswordHasher
import mu.master.identity.views.IIdentityView

class IdentityService(
        private val identityView: IIdentityView,
        private val passwordHasher: IPasswordHasher,
        private val authTokenCreator: IAuthTokenCreator
) : IIdentityService {

    override fun doesUserExist(userId: UserId): Boolean {
        return identityView.userAccountsById[userId.value] != null
    }

    override fun createAuthenticationToken(email: String, password: String): AuthToken? {
        val userAccount = identityView.userAccountsByEmail[email] ?: throw IllegalArgumentException("Unknown email")
        val passwordHash = identityView.passwordHashesByEmail[email] ?: throw IllegalArgumentException("Unknown email")

        // TODO too much work in application layer?
        val isAuthenticated = passwordHasher.verifyHash(password, PasswordHash(passwordHash))
        if (isAuthenticated) {
            return authTokenCreator.createToken(UserId(userAccount.id))
        }

        return null
    }
}

