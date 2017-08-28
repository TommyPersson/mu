package mu.master

import mu.master.teams_and_users.domain.UserId
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.auth.Principal
import org.jetbrains.ktor.auth.authentication
import org.jetbrains.ktor.auth.basicAuthentication
import org.jetbrains.ktor.request.receiveText
import org.jetbrains.ktor.routing.*


fun Application.setupRoutes(): Routing {
    return routing {
        setupApiRoutes()
        setupGraphQlRoutes()
    }
}

private fun Routing.setupGraphQlRoutes() {
    route("graphql") {
        setupAuthentication()

        post {
            callGraphQLApi(call)
        }
    }
}

private fun Routing.setupApiRoutes() {

    apiActionsMap.values.forEach { action ->
        route("api/${action.actionId}") {
            if (action.requiresAuthentication) {
                setupAuthentication()
            }

            post {
                callApiAction(action.actionId, call)
            }
        }
    }
}

class MuUserPrincipal(val userId: UserId) : Principal

private fun Route.setupAuthentication() {
    authentication {
        basicAuthentication("mu.master") { (email, password) ->
            val userAccountRepository = DI.Identity.userAccountRepository
            val userId = DI.Identity.identityView.userAccountsByEmail[email]?.id ?: return@basicAuthentication null
            val user = userAccountRepository.getById(userId) ?: return@basicAuthentication null

            val isAuthenticated = user.authenticate(password, DI.Identity.passwordHasher)

            if (isAuthenticated) {
                MuUserPrincipal(UserId(userId))
            } else null
        }
    }
}