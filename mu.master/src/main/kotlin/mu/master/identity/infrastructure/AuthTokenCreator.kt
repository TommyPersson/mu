package mu.master.identity.infrastructure

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.crypto.MacProvider
import mu.libs.utils.IClock
import mu.libs.utils.toClassicDate
import mu.master.identity.domain.AuthToken
import mu.master.identity.domain.UserId
import mu.master.identity.domain.services.IAuthTokenCreator
import mu.master.identity.views.IIdentityView
import java.io.File
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class AuthTokenCreator(
        private val identityView: IIdentityView,
        private val clock: IClock
) : IAuthTokenCreator {

    private val signingAlgo = SignatureAlgorithm.HS512
    private var _signingKey: SecretKey? = null

    override val signingKey: SecretKey get() {
        if (_signingKey == null) {
            // TODO abstract filesystem ()
            val keyFile = File(System.getProperty("user.home"), ".mu/master/config/jwt-signing-key-base64.txt").apply {
                parentFile.mkdirs()
            }

            if (!keyFile.exists()) {
                val newKey = MacProvider.generateKey()
                keyFile.writeText(Base64.getEncoder().encodeToString(newKey.encoded))
            }

            val keyBytes = Base64.getDecoder().decode(keyFile.readText())
            _signingKey = SecretKeySpec(keyBytes, signingAlgo.jcaName)
        }

        return _signingKey!!
    }

    override fun createToken(userId: UserId): AuthToken {
        val userAccount = identityView.userAccountsById[userId.value] ?: throw IllegalArgumentException("Unknown userId: <$userId>")

        val expiration = clock.utcNow().plusDays(1)

        val jws = Jwts.builder()
                .setClaims(mapOf(
                        "displayName" to userAccount.displayName,
                        "email" to userAccount.email))
                .setSubject(userAccount.id.toString())
                .setExpiration(expiration.toClassicDate())
                .signWith(signingAlgo, signingKey)
                .compact()

        return AuthToken(jws)
    }

}