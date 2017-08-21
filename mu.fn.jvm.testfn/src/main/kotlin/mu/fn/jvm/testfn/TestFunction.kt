package mu.fn.jvm.testfn

import mu.fn.jvm.api.IContext
import mu.fn.jvm.api.IFunction

class TestFunction : IFunction {
    override fun invoke(input: String, context: IContext): String {
        return input.reversed()
    }
}