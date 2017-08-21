package mu.node.execution

import mu.fn.platform.base.FunctionMetadata
import java.io.File

interface IFunctionFileCache {
    suspend fun getFunctionFile(metadata: FunctionMetadata): File
}