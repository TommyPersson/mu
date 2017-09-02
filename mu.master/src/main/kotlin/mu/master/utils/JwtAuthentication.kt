package mu.master.utils

import io.jsonwebtoken.*
import org.jetbrains.ktor.auth.*
import org.jetbrains.ktor.request.ApplicationRequest
import org.jetbrains.ktor.request.header
import org.jetbrains.ktor.response.respond
import javax.crypto.SecretKey


data class JwtClaimsCredential(val token: Jws<Claims>) : Credential

val JwtClaimsKey: Any = "JwtClaims"
fun AuthenticationPipeline.jwtClaimsAuthentication(key: SecretKey, validate: suspend (JwtClaimsCredential) -> Principal?) {
    intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val jwt = call.request.jwtTokenText()
        val credentials = jwt?.let { parseJwtClaimsCredentials(key, it) }
        val principal = credentials?.let { validate(it) }

        val cause = when {
            jwt == null -> NotAuthenticatedCause.NoCredentials
            credentials == null -> NotAuthenticatedCause.InvalidCredentials
            principal == null -> NotAuthenticatedCause.InvalidCredentials
            else -> null
        }

        if (cause != null) {
            context.challenge(JwtClaimsKey, cause) {
                it.success()
                call.respond(UnauthorizedResponse())
            }
        }
        if (principal != null) {
            context.principal(principal)
        }
    }
}

fun ApplicationRequest.jwtTokenText(): String? {
    return header("Authorization")?.substringAfter("Bearer ")
}

fun parseJwtClaimsCredentials(key: SecretKey, jwt: String): JwtClaimsCredential? {
    val jws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt)

    return JwtClaimsCredential(jws)

}