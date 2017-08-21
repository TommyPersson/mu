package mu.node

import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.netty.Netty

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        embeddedServer(Netty, Config.port) {
            install(GsonSupport)
            setupRoutes()
        }.start(wait = true)
    }
}

