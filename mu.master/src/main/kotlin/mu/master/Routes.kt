package mu.master

import mu.master.identity.domain.UserId
import mu.master.utils.jwtClaimsAuthentication
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.auth.Principal
import org.jetbrains.ktor.auth.authentication
import org.jetbrains.ktor.routing.*
import java.util.*


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

class MuUserPrincipal(val userId: UUID) : Principal

private fun Route.setupAuthentication() {
    authentication {
        jwtClaimsAuthentication(DI.Identity.authTokenCreator.signingKey) { (jwt) ->
            val identityService = DI.Identity.identityService
            val userId = UUID.fromString(jwt.body.subject)

            if (identityService.doesUserExist(UserId(userId))) {
                MuUserPrincipal(userId)
            } else {
                null
            }
        }
    }
}

