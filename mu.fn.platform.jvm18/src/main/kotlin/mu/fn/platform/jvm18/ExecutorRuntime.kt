package mu.fn.platform.jvm18

import mu.fn.platform.base.FunctionMetadata
import java.io.File

class ExecutorRuntime(
        metadata: FunctionMetadata,
        jar: File,
        functionLoader: IFunctionLoader
) {
    private val function = functionLoader.load(jar)

    suspend fun invokeFunction(input: String): String {
        return function.invoke(input, Context())
    }
}
