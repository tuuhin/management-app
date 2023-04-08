package com.example.utils.forms

import io.ktor.http.*

data class SignUpForm(
    val userName: String,
    val email: String,
    val password1: String,
    val password2: String
) {
    companion object {
        fun fromParameters(params: Parameters): SignUpForm {
            return SignUpForm(
                userName = params["username"] ?: "",
                email = params["email"] ?: "",
                password1 = params["password1"] ?: "",
                password2 = params["password2"] ?: ""
            )
        }
    }
}
