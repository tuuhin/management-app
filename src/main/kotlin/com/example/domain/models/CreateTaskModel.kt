package com.example.domain.models

data class CreateTaskModel(
    val title: String,
    val desc: String? = null,
    val user: UserModel
)
