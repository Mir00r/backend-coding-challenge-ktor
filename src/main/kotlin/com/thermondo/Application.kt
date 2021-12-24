package com.thermondo

import com.thermondo.authentication.JwtConfig
import com.thermondo.domains.todos.repositories.TodoRepository
import com.thermondo.domains.users.repositories.InMemoryUserRepository
import com.thermondo.routes.todos
import com.thermondo.routes.users
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.locations.*
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.json

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val jwtConfig = JwtConfig()

// NOTE: Referenced in application.conf
@KtorExperimentalLocationsAPI
@Suppress("unused")
fun Application.module(testing: Boolean = false) {

    install(CallLogging)
    install(ContentNegotiation) {
        json()
        gson {
            setPrettyPrinting()
        }
    }

    install(Authentication) {
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }

    routing {
        val userRepository = InMemoryUserRepository()
        val todoRepository = TodoRepository()

        get("/") {
            call.respondText("Hello World!")
        }
        users(userRepository)
        todos(todoRepository)
    }
}
