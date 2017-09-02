package mu.master

import com.google.gson.GsonBuilder
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CORS
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.HttpHeaders
import org.jetbrains.ktor.netty.Netty

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        DI.Identity.identityView.initialize()
        DI.TeamsAndUsers.teamsView.initialize()

        // TODO make view.initialize block until caught up

        embeddedServer(Netty, Config.port) {
            install(GsonSupport) { configureGson() }
            install(CORS) {
                anyHost()
                allowCredentials = true
                header(HttpHeaders.Authorization)
            }
            setupRoutes()
        }.start(wait = true)
    }
}

fun GsonBuilder.configureGson() {
    setPrettyPrinting()
}

