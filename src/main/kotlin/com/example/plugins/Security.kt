package com.example.plugins

import com.example.domain.models.UserModel
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import io.ktor.util.*

fun Application.configureSecurity() {
    install(Sessions) {
        // this is the secret signing key make sure it's changed latter
        // This project is not that important thus using these
        val secretSignKey = hex("0123456789abcdef")
        // encryption key should be 16 bit long
        val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
        cookie<UserModel>("sessions") {
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
            cookie.extensions["SameSite"] = "lax"
            cookie.httpOnly = true
            cookie.maxAge = null
        }
    }
}
