package mu.master.utils

import mu.master.ApiAction
import mu.master.teams_and_users.domain.UserId


class ActionsBuilder {
    val actions = mutableListOf<ApiAction<*, *>>()

    inline fun <reified I : Any, reified O : Any> actionOf(id: String, crossinline actionFn: (I, UserId) -> O) {
        val action = object : ApiAction<I, O>(id, I::class) {
            suspend override fun invoke(userId: UserId, input: I): O {
                return actionFn(input, userId)
            }
        }

        actions.add(action)
    }

    fun build() = actions
}

fun createActions(actionsCreator: ActionsBuilder.() -> Unit): List<ApiAction<*, *>> {
    val builder = ActionsBuilder()
    actionsCreator(builder)
    return builder.build()
}