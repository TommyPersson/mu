package mu.master

import mu.master.teams_and_users.application.api.teamsAndUsersApiActions
import mu.master.teams_and_users.domain.UserId
import org.jetbrains.ktor.application.ApplicationCall
import org.jetbrains.ktor.auth.principal
import org.jetbrains.ktor.request.tryReceive
import org.jetbrains.ktor.response.respond
import kotlin.reflect.KClass


abstract class ApiAction<I : Any, out O>(
        val actionId: String,
        val inputType: KClass<I>) {

    suspend abstract fun invoke(userId: UserId, input: I): O
}

private val actionMap = listOf(
        teamsAndUsersApiActions
).flatten().associateBy { it.actionId }

@Suppress("UNCHECKED_CAST")
suspend fun callApiAction(actionId: String, call: ApplicationCall) {
    val action = actionMap[actionId] as? ApiAction<Any,Any> ?: throw Exception("no such action")
    val actionInputType = action.inputType
    val input = call.tryReceive(actionInputType) ?: throw Exception("unable to parse input")
    val userId = call.principal<MuUserPrincipal>()?.userId ?: throw Exception("not authenticated")

    val output = action.invoke(userId, input)

    call.respond(output)
}