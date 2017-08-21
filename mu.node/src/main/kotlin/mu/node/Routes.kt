package mu.node

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import mu.node.execution.*
import mu.node.repository.FakeRepositoryProxy
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.request.receiveText
import org.jetbrains.ktor.response.contentType
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Routing
import org.jetbrains.ktor.routing.post
import org.jetbrains.ktor.routing.routing

val gson = Gson()
val repositoryProxy = FakeRepositoryProxy()
val functionFileCache = FunctionFileCache(repositoryProxy)
val functionProxyFactory = FunctionProxyFactory()
val executor: IFunctionExecutor = FunctionExecutor(repositoryProxy, functionFileCache, functionProxyFactory)

fun Application.setupRoutes(): Routing {
    return routing {
        post("/execute-fn") {
            //call.tryReceive<ExecutionParameters>()
            val content = call.receiveText()
            val params = gson.fromJson<ExecutionParameters>(content)
            val result = executor.executeFunction(params)

            call.response.contentType(ContentType.Application.Json)
            call.respond(result)
        }
    }
}


