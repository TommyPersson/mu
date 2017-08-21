package mu.node.repository

import mu.fn.platform.base.FunctionMetadata
import java.io.File
import java.io.InputStream
import java.util.*

class FakeRepositoryProxy : IRepositoryProxy {

    suspend override fun getMetadata(id: UUID, version: Int): FunctionMetadata {
        return FunctionMetadata(
                id = id,
                name = "my test fn",
                version = version,
                fileName = "function.jar",
                platformKey = "jvm-1.8")
    }

    suspend override fun getFileData(id: UUID, version: Int): InputStream {
        return File("C:\\Users\\Tommy\\test\\function.jar").inputStream()
    }
}