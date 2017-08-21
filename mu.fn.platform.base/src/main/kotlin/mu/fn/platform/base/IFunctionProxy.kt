package mu.fn.platform.base

interface IFunctionProxy {
    suspend fun invoke(data: String): String

    suspend fun pause() {}

    suspend fun resume() {}

    suspend fun dispose() {}
}