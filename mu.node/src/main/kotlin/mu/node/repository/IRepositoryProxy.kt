package mu.node.repository

import mu.fn.platform.base.FunctionMetadata
import java.io.InputStream
import java.util.*

interface IRepositoryProxy {
    suspend fun getMetadata(id: UUID, version: Int): FunctionMetadata

    suspend fun getFileData(id: UUID, version: Int): InputStream
}

