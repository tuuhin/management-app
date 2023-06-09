package com.example.plugins

import com.example.routes.authRoute
import com.example.routes.homeRoutes
import com.example.routes.taskRoutes
import com.example.utils.constants.Paths
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        route(Paths.AUTH_PATH) { authRoute() }
        route(Paths.HOME_PATH) {
            homeRoutes()
            taskRoutes()
        }

    }
}
