package com.example.routes

import com.example.domain.facades.TaskDaoFacade
import com.example.domain.models.UserModel
import com.example.domain.validators.TaskFormValidator
import com.example.domain.validators.Validator
import com.example.utils.constants.Paths
import com.example.utils.forms.CreateTaskForm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.homeRoutes() {

    val taskFacade by inject<TaskDaoFacade>()

    route(Paths.HOME_PATH) {
        get {
            val user = call.sessions.get<UserModel>()
            if (user != null) {
                val tasks = taskFacade.getTasks(user)
                call.respond(
                    PebbleContent(
                        template = "index.html",
                        mapOf("user" to user, "tasks" to tasks),
                        contentType = ContentType.Text.Html
                    )
                )
                return@get
            }
            call.respondRedirect(Paths.FULL_LOGIN_ROUTE)
        }
    }
    route(Paths.ADD_TASK) {
        get {
            val user = call.sessions.get<UserModel>()
            if (user != null) {
                call.respond(PebbleContent("create-task.html", mapOf("user" to user)))
                return@get
            }
            call.respondRedirect(Paths.FULL_LOGIN_ROUTE)
        }
        post {
            val user = call.sessions.get<UserModel>()
            if (user != null) {
                val params = call.receiveParameters()
                val form = CreateTaskForm.fromParameters(params)
                val validator = TaskFormValidator.execute(form)
                if (!validator.isValid) {
                    call.respond(
                        PebbleContent(
                            template = "create-task.html",
                            mapOf("validation_error" to validator, "user" to user)
                        )
                    )
                    return@post
                }
                val createModel = form.toModel(user)
                val newTask = taskFacade.createTask(createModel)
                if (newTask != null) {
                    call.respondRedirect(Paths.HOME_PATH)
                    return@post
                }
                val failed = Validator(isValid = false, message = "Cannot create content")
                call.respond(
                    PebbleContent(
                        template = "create-task.html",
                        mapOf("validation_error" to failed, "user" to user)
                    )
                )
                return@post
            }
            call.respondRedirect(Paths.FULL_LOGIN_ROUTE)
        }
    }
}