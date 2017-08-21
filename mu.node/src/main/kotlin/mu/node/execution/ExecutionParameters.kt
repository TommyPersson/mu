package mu.node.execution

import java.util.*

data class ExecutionParameters(
        val functionId: UUID,
        val functionVersion: Int,
        val data: String)