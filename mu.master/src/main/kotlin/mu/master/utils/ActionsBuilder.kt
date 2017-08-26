package mu.master.utils

import mu.master.ActionContext
import mu.master.ApiAction


class ActionsBuilder {
    val actions = mutableListOf<ApiAction<*, *>>()

    inline fun <reified I : Any, reified O : Any> actionOf(id: String, crossinline actionFn: (I, ActionContext) -> O) {
        val action = object : ApiAction<I, O>(id, I::class) {
            suspend override fun invoke(input: I, context: ActionContext): O {
                return actionFn(input, context)
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