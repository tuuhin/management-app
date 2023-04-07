package com.example.domain.models

import java.time.LocalDate

@OptIn(ExperimentalUnsignedTypes::class)
data class UserAuthModel(
    val id: Int,
    val username: String,
    val email: String? = null,
    val passwordHash: UByteArray,
    val createdAt: LocalDate
) {
    fun toModel(): UserModel = UserModel(
        id = id,
        username = username,
        email = email
    )
}