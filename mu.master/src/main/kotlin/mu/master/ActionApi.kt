package mu.master

import mu.master.identity.application.api.identityContextActions
import mu.master.teams_and_users.application.api.teamsAndUsersContextActions
import org.jetbrains.ktor.application.ApplicationCall
import org.jetbrains.ktor.auth.UnauthorizedResponse
import org.jetbrains.ktor.auth.principal
import org.jetbrains.ktor.request.tryReceive
import org.jetbrains.ktor.response.respond
import kotlin.reflect.KClass


class ActionContext(
        val userPrincipal: MuUserPrincipal?
)

abstract class ApiAction<I : Any, out O>(
        val actionId: String,
        val requiresAuthentication: Boolean,
        val inputType: KClass<I>) {

    suspend abstract fun invoke(input: I, context: ActionContext): O
}

val apiActionsMap = listOf(
        teamsAndUsersContextActions,
        identityContextActions
).flatten().associateBy { it.actionId }

@Suppress("UNCHECKED_CAST")
suspend fun callApiAction(actionId: String, call: ApplicationCall) {
    val action = apiActionsMap[actionId] as? ApiAction<Any,Any> ?: throw Exception("no such action")

    val principal = call.principal<MuUserPrincipal>()

    if (action.requiresAuthentication && principal == null) {
        call.respond(UnauthorizedResponse())
        return
    }

    val actionInputType = action.inputType
    val input = call.tryReceive(actionInputType) ?: throw Exception("unable to parse input")

    val context = ActionContext(principal)

    val output = action.invoke(input, context)

    call.respond(output)
}