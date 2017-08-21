package mu.node.execution

import mu.fn.platform.base.FunctionMetadata
import mu.fn.platform.base.IFunctionProxy
import java.io.File

interface IFunctionProxyFactory {
    suspend fun create(metadata: FunctionMetadata, functionFile: File): IFunctionProxy
}