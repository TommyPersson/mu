package mu.node.execution

import mu.node.repository.IRepositoryProxy

class FunctionExecutor(
        private val repository: IRepositoryProxy,
        private val functionFileCache: IFunctionFileCache,
        private val functionProxyFactory: IFunctionProxyFactory
) : IFunctionExecutor {

    override suspend fun executeFunction(params: ExecutionParameters): ExecutionResult {
        val metadata = repository.getMetadata(params.functionId, params.functionVersion)
        val functionFile = functionFileCache.getFunctionFile(metadata)
        val data = params.data

        val functionProxy = functionProxyFactory.create(metadata, functionFile)
        val resultData = functionProxy.invoke(data)

        return ExecutionResult(resultData)
    }
}
