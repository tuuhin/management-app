package com.example.domain.models

import java.time.LocalDateTime

data class UpdateTaskModel(
    val id: Int,
    val title: String,
    val desc: String? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val status: TaskStatus
)