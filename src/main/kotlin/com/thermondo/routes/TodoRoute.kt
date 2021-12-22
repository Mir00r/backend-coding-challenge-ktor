package com.thermondo.routes

import com.thermondo.authentication.JwtConfig
import com.thermondo.commons.Constants
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/12/21
 */
const val TODOS = Constants.API_VERSION + "/todos"

@KtorExperimentalLocationsAPI
@Location(TODOS)
class TodoRoute


@KtorExperimentalLocationsAPI
fun Route.todos(db: Repository) {
    authenticate("jwt") {
        post<TodoRoute> {
            val todosParameters = call.receive<Parameters>()
            val title = todosParameters["title"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Title")
            val body = todosParameters["body"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Body")
            val tags = todosParameters["tags"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Tags")

            if (call.authentication.principal == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@post
            }
            val user = call.authentication.principal as JwtConfig.JwtUser

            try {
                val currentTodo = db.addTodo(user.userId, title, done.toBoolean())
                currentTodo?.id?.let {
                    call.respond(HttpStatusCode.OK, currentTodo)
                }
            } catch (e: Throwable) {
                application.log.error("Failed to add todo", e)
                call.respond(HttpStatusCode.BadRequest, "Problems Saving Todo")
            }
        }
        get<TodoRoute> {
            if (call.authentication.principal == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@get
            }
            val user = call.authentication.principal as JwtConfig.JwtUser

            val todosParameters = call.request.queryParameters
            val limit = if (todosParameters.contains("limit")) todosParameters["limit"] else null
            val offset = if (todosParameters.contains("offset")) todosParameters["offset"] else null
            try {
                if (limit != null && offset != null) {
                    val todos = db.getTodos(user.userId, offset.toInt(), limit.toInt())
                    call.respond(todos)

                } else {
                    val todos = db.getTodos(user.userId)
                    call.respond(todos)
                }
            } catch (e: Throwable) {
                application.log.error("Failed to get Todos", e)
                call.respond(HttpStatusCode.BadRequest, "Problems getting Todos")
            }
        }
        delete<TodoRoute> {
            val todosParameters = call.receive<Parameters>()
            if (!todosParameters.contains("id")) {
                return@delete call.respond(HttpStatusCode.BadRequest, "Missing Todo Id")
            }
            val todoId =
                todosParameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing Todo Id")
            if (call.authentication.principal == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@delete
            }
            val user = call.authentication.principal as JwtConfig.JwtUser

            try {
                db.deleteTodo(user.userId, todoId.toInt())
                call.respond(HttpStatusCode.OK)
            } catch (e: Throwable) {
                application.log.error("Failed to delete todo", e)
                call.respond(HttpStatusCode.BadRequest, "Problems Deleting Todo")
            }
        }
    }
}
