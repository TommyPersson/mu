@file:Suppress("unused")

package mu.fn.jvm.api

interface IFunction {
    fun invoke(input: String, context: IContext): String
}
