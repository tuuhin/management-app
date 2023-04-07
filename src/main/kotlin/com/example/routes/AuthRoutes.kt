package com.example.routes

import com.example.domain.facades.UserDaoFacade
import com.example.domain.validators.SignUpValidator
import com.example.utils.Validator
import com.example.utils.forms.SignUpForm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoute() {
    val userDao by inject<UserDaoFacade>()

    route("login") {
        get {
            call.respond(PebbleContent("login.html", emptyMap()))
        }
    }
    route("signup") {

        get {
            call.respond(
                PebbleContent(
                    "sign_up.html",
                    emptyMap(),
                    contentType = ContentType.Text.Html,
                    etag = "sign_up"
                )
            )
        }
        post {
            val params = call.receiveParameters()
            val form = SignUpForm(
                userName = params["username"] ?: "",
                email = params["email"] ?: "",
                password1 = params["password1"] ?: "",
                password2 = params["password2"] ?: ""
            )
            val validation = SignUpValidator.validate(form)
            if (!validation.isValid) {
                call.respond(
                    PebbleContent(
                        template = "sign_up.html",
                        mapOf("validation_error" to validation)
                    )
                )
                return@post
            }
            val checkUser = userDao.getUser(userName = form.userName, email = form.email)
            if (checkUser != null) {
                val userValidator = Validator(isValid = false, message = "User exists with same credentials")
                call.respond(
                    PebbleContent(
                        template = "sign_up.html",
                        mapOf("validation_error" to userValidator)
                    )
                )
                return@post
            }
            call.respond(
                PebbleContent(
                    template = "sign_up.html",
                    emptyMap(),
                    contentType = ContentType.Text.Html
                )
            )
        }
    }
}