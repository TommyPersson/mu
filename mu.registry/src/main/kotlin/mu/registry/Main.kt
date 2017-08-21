package mu.registry

import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.delete
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.post
import org.jetbrains.ktor.routing.routing

fun main(args: Array<String>) {
    val registry = Registry(Account()) // how to do per-request?

    embeddedServer(Netty, 8080) {
        routing {
            get("/function") {
                val functionNames = registry.listFunctions()
                call.respondText(functionNames.joinToString(), ContentType.Text.Plain)
            }
            post("/function/{name}") {
                val functionName = call.parameters["name"] ?: throw IllegalArgumentException("name")
                registry.createFunction(functionName)
                call.respond(Any())
            }
            delete("/function/{name}") {
                val functionName = call.parameters["name"] ?: throw IllegalArgumentException("name")
                registry.deleteFunction(functionName)
                call.respond(Any())
            }
        }
    }.start(wait = true)
}