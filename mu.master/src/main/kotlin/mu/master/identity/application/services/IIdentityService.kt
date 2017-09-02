package mu.master.identity.application.services

import mu.master.identity.domain.AuthToken
import mu.master.identity.domain.UserId

interface IIdentityService {
    fun doesUserExist(userId: UserId): Boolean

    fun createAuthenticationToken(email: String, password: String): AuthToken?

}

