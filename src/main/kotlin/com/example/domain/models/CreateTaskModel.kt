package com.example.domain.models

data class CreateTaskModel(
    val title: String,
    val desc: String? = null,
    val user: UserModel,
    val status: TaskStatus
) {
    fun fromCreateModel(id: Int): UpdateTaskModel {
        return UpdateTaskModel(
            id = id,
            title = title,
            desc = desc,
            status = status
        )
    }
}