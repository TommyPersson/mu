package mu.master

import mu.master.teams_and_users.application.api.teamsAndUsersApiActions
import org.jetbrains.ktor.application.ApplicationCall
import org.jetbrains.ktor.auth.principal
import org.jetbrains.ktor.request.tryReceive
import org.jetbrains.ktor.response.respond
import kotlin.reflect.KClass


class ActionContext(
        val userPrincipal: MuUserPrincipal
)

abstract class ApiAction<I : Any, out O>(
        val actionId: String,
        val inputType: KClass<I>) {

    suspend abstract fun invoke(input: I, context: ActionContext): O
}

private val actionMap = listOf(
        teamsAndUsersApiActions
).flatten().associateBy { it.actionId }

@Suppress("UNCHECKED_CAST")
suspend fun callApiAction(actionId: String, call: ApplicationCall) {
    val action = actionMap[actionId] as? ApiAction<Any,Any> ?: throw Exception("no such action")
    val actionInputType = action.inputType
    val input = call.tryReceive(actionInputType) ?: throw Exception("unable to parse input")

    val userPrincipal = call.principal<MuUserPrincipal>() ?: throw Exception("not authenticated")
    val context = ActionContext(userPrincipal)

    val output = action.invoke(input, context)

    call.respond(output)
}