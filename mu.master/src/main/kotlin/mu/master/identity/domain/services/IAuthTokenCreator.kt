package mu.master.identity.domain.services

import mu.master.identity.domain.AuthToken
import mu.master.identity.domain.UserId
import javax.crypto.SecretKey

interface IAuthTokenCreator {
    val signingKey: SecretKey

    fun createToken(userId: UserId): AuthToken
}