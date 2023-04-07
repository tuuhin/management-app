package com.example.domain.models

data class UserModel(
    val id: Int,
    val username: String,
    val email: String? = null,
)