package mu.node.execution

import mu.fn.platform.base.FunctionMetadata
import mu.node.repository.IRepositoryProxy
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class FunctionFileCache(
        private val repository: IRepositoryProxy
) : IFunctionFileCache {

    private val workDir = File(System.getProperty("user.home"), ".mu-node/function-file-cache").apply {
        mkdirs()
    }

    suspend override fun getFunctionFile(metadata: FunctionMetadata): File {
        val functionFile = File(File(File(workDir, metadata.id.toString()), metadata.version.toString()), metadata.fileName).apply {
            parentFile.mkdirs()
        }

        if (!functionFile.exists()) {
            val tempFile = Files.createTempFile("mu-fn", null).toFile()
            try {
                val functionInputStream = repository.getFileData(metadata.id, metadata.version)
                functionInputStream.use { input ->
                    tempFile.outputStream().use { output ->
                        val b = input.copyTo(output)
                        println(b)
                    }
                }

                Files.move(tempFile.toPath(), functionFile.toPath(), StandardCopyOption.ATOMIC_MOVE)
            } finally {
                tempFile.delete()
            }
        }

        if (!functionFile.exists()) {
            throw Exception("Unable to location function file!")
        }

        return functionFile
    }
}