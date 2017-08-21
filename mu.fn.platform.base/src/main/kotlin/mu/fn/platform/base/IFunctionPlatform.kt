package mu.fn.platform.base

import java.io.File

interface IFunctionPlatform {

    val key: String
    val name: String
    val description: String

    suspend fun createProxy(metadata: FunctionMetadata, file: File): IFunctionProxy
}