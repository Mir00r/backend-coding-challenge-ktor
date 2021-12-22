package com.thermondo

import com.thermondo.authentication.JwtConfig
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.ContentNegotiation
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.json

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val jwtConfig = JwtConfig(System.getenv("KTOR_TODOLIST_JWT_SECRET"))

// NOTE: Referenced in application.conf
@Suppress("unused")
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation) {
        json()
    }

    install(Authentication) {
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
