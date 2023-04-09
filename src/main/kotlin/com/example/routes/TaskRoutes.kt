package com.example.routes

import com.example.domain.facades.TaskDaoFacade
import com.example.domain.models.UserModel
import com.example.domain.validators.TaskFormValidator
import com.example.domain.validators.Validator
import com.example.utils.constants.Paths
import com.example.utils.forms.CreateTaskForm
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.taskRoutes() {

    val taskDaoFacade by inject<TaskDaoFacade>()

    route("${Paths.UPDATE_ROUTE}/{id}") {
        get {
            val user = call.sessions.get<UserModel>()
            if (user == null) {
                call.respondRedirect(Paths.FULL_LOGIN_ROUTE, permanent = true)
                return@get
            }
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondRedirect(Paths.HOME_PATH, permanent = true)
                return@get
            }
            val task = taskDaoFacade.getTaskById(id)
            if (task == null) {
                call.respondRedirect(Paths.HOME_PATH, permanent = true)
                return@get
            }
            call.respond(PebbleContent("update-task.html", mapOf("user" to user, "task" to task)))
        }
        post {
            val user = call.sessions.get<UserModel>()
            if (user != null) {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respondRedirect(Paths.HOME_PATH)
                    return@post
                }
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
                val createModel = form.toModel(user).fromCreateModel(id)
                val newTask = taskDaoFacade.updateTask(createModel)
                if (newTask != null) call.respondRedirect(Paths.HOME_PATH)
                else call.respond(
                    PebbleContent(
                        template = "create-task.html",
                        mapOf(
                            "validation_error" to Validator(isValid = false, message = "Cannot create content"),
                            "user" to user
                        )
                    )
                )
                return@post
            }
            call.respondRedirect(Paths.HOME_PATH, permanent = true)
        }
    }

    route("${Paths.DELETE_ROUTE}/{id}") {
        get {
            val user = call.sessions.get<UserModel>()
            if (user == null) {
                call.respondRedirect(Paths.FULL_LOGIN_ROUTE)
                return@get
            }
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondRedirect(Paths.HOME_PATH, permanent = true)
                return@get
            }
            val delete = taskDaoFacade.deleteTask(model = user, id)
            if (delete == 0) {
                call.respondRedirect(Paths.HOME_PATH, permanent = true)
                return@get
            }
            call.respondRedirect(Paths.HOME_PATH, permanent = true)
        }
    }
}