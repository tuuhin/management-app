package com.example.domain.models

import java.time.LocalDateTime

data class TaskModel(
    val id: Int,
    val title: String,
    val desc: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val status: TaskStatus
)
