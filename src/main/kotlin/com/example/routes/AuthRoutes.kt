package com.example.routes

import com.example.domain.facades.UserDaoFacade
import com.example.domain.models.UserModel
import com.example.domain.validators.LoginValidator
import com.example.domain.validators.SignUpValidator
import com.example.utils.constants.Paths
import com.example.domain.validators.Validator
import com.example.utils.forms.SignUpForm
import com.example.utils.forms.UserLoginForm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.authRoute() {
    val userDao by inject<UserDaoFacade>()

    route(Paths.LOGIN_PATH) {
        get {
            val user = call.sessions.get<UserModel>()
            if (user != null) {
                call.respondRedirect(url = Paths.HOME_PATH)
                return@get
            }
            call.respond(PebbleContent("login.html", emptyMap(), contentType = ContentType.Text.Html))
        }
        post {
            val params = call.receiveParameters()
            val form = UserLoginForm.fromParameter(params)
            val validator = LoginValidator.validate(form)
            if (!validator.isValid) {
                call.respond(
                    PebbleContent(
                        template = "login.html",
                        mapOf("validation_error" to validator),
                        contentType = ContentType.Text.Html
                    )
                )
                return@post
            }
            val checkUser = userDao.checkUser(form.userName, form.password, form.email)
            if (checkUser != null) {
                call.sessions.set(checkUser)
                call.respondRedirect(Paths.HOME_PATH)
                return@post
            }
            val checkFailed = Validator(false, "User not found")
            call.respond(
                PebbleContent(
                    template = "login.html",
                    mapOf("validation_error" to checkFailed),
                    contentType = ContentType.Text.Html
                )
            )
        }
    }
    route(Paths.SIGN_UP_PATH) {
        get {
            val user = call.sessions.get<UserModel>()
            if (user != null) {
                call.respondRedirect(Paths.HOME_PATH)
                return@get
            }
            call.respond(
                PebbleContent("sign_up.html", emptyMap(), contentType = ContentType.Text.Html)
            )
        }
        post {
            val params = call.receiveParameters()
            val form = SignUpForm.fromParameters(params)
            val validation = SignUpValidator.validate(form)
            if (!validation.isValid) {
                call.respond(
                    PebbleContent(
                        template = "sign_up.html",
                        mapOf("validation_error" to validation),
                        contentType = ContentType.Text.Html
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
                        mapOf("validation_error" to userValidator),
                        contentType = ContentType.Text.Html
                    )
                )
                return@post
            }
            val createUser = userDao.createUser(
                userName = form.userName,
                email = form.email,
                password = form.password1
            )
            if (createUser != null) {
                call.sessions.set(createUser)
                call.respondRedirect(url = Paths.HOME_PATH)
                return@post
            }
            call.respond(
                PebbleContent(
                    template = "sign_up.html",
                    mapOf(
                        "validation_error" to Validator(false, "unknown one")
                    )
                )
            )
        }
    }
    route(Paths.LOGOUT_PATH) {
        get {
            call.sessions.clear<UserModel>()
            call.respondRedirect(url = Paths.HOME_PATH)
        }
    }
}