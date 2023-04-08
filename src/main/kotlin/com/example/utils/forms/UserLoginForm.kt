package com.example.utils.forms

import io.ktor.http.*

data class UserLoginForm(
    val userName: String,
    val email: String? = null,
    val password: String
) {
    companion object {
        fun fromParameter(params: Parameters): UserLoginForm {
            return UserLoginForm(
                userName = params["username"] ?: "",
                email = params["email"],
                password = params["password"] ?: ""
            )
        }
    }
}
