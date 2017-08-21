package mu.fn.platform.jvm18

import mu.fn.platform.base.FunctionMetadata
import mu.fn.platform.base.IFunctionPlatform
import mu.fn.platform.base.IFunctionProxy
import java.io.File

class Jvm18FunctionPlatform : IFunctionPlatform {

    override val key = "jvm-1.8"
    override val name = "JVM 1.8"
    override val description = "Executes JVM 1.8 function JARs"

    suspend override fun createProxy(metadata: FunctionMetadata, file: File): IFunctionProxy {
        return object : IFunctionProxy {

            val exec = ExecutorRuntime(metadata, file, FunctionLoader())

            suspend override fun invoke(data: String): String {
                return exec.invokeFunction(data)
            }
        }
    }
}