package mu.master

import de.mkammerer.argon2.Argon2Factory
import mu.master.teams_and_users.domain.Password
import mu.master.teams_and_users.domain.UserId
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.auth.Principal
import org.jetbrains.ktor.auth.authentication
import org.jetbrains.ktor.auth.basicAuthentication
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.request.receiveText
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.*
import java.util.*

class MuUserPrincipal(val userId: UserId) : Principal

val argon2 = Argon2Factory.create()

val user1Id = UserId(UUID.fromString("ea678a8c-d56c-4ad0-9e52-c3dd939d5e1a"))
val user1Salt = "1234"
val user1Hash = argon2.hash(3, 12, 1, user1Salt + "hello")

data class AuthDbEntry(
        val userName: String,
        val userId: UserId,
        val password: Password
)

val userDb = listOf(
        AuthDbEntry("test1", user1Id, Password(user1Hash, user1Salt))
).associateBy { it.userName }


fun Application.setupRoutes(): Routing {
    return routing {
        authentication {
            basicAuthentication("mu.master") { (name, password) ->
                userDb[name]?.let { entry ->
                    if (argon2.verify(entry.password.argon2Hash, entry.password.salt + password)) {
                        MuUserPrincipal(entry.userId)
                    } else null
                }
            }
        }

        setupApiRoutes()
        setupGraphQlRoutes()
    }
}

private fun Routing.setupGraphQlRoutes() {
    post("graphql") {
        callGraphQLApi(call.receiveText(), call)
    }
}

private fun Routing.setupApiRoutes() {
    route("api") {
        post("{actionId}") {
            val actionId = call.parameters["actionId"] ?: throw Exception("no action specified")
            callApiAction(actionId, call)
        }
    }
}