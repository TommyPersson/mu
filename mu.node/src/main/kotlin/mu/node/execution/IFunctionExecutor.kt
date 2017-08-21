package mu.node.execution

interface IFunctionExecutor {
    suspend fun executeFunction(params: ExecutionParameters): ExecutionResult
}