package mu.fn.platform.base

import java.util.*

data class FunctionMetadata(
        val id: UUID,
        val name: String,
        val version: Int,
        val fileName: String,
        val platformKey: String)