package com.thermondo.routes

import com.thermondo.authentication.JwtConfig
import com.thermondo.commons.Constants
import com.thermondo.domains.users.models.dtos.LoginDto
import com.thermondo.domains.users.repositories.IUserRepository
import com.thermondo.jwtConfig
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/12/21
 */

const val USERS = Constants.API_VERSION + "/users"
const val USER_LOGIN = "$USERS/login"

@KtorExperimentalLocationsAPI
@Location(USER_LOGIN)
class UserLoginRoute


@KtorExperimentalLocationsAPI
fun Route.users(db: IUserRepository) {
    post<UserLoginRoute> {
        val loginBody = call.receive<LoginDto>()

        val user = db.getUser(loginBody.username, loginBody.password)
        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid credentials!")
            return@post
        }
        val token = jwtConfig.generateToken(JwtConfig.JwtUser(user.userId, user.username))
        call.respond(token)
    }
}
