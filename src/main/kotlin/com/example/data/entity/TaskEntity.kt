package com.example.data.entity

import com.example.domain.models.TaskStatus
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TaskEntity : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 100)
    val desc = text("desc").nullable()
    val createdAt = datetime("createdAt")
    val updatedAt = datetime("updatedAt")
    val user = (integer("user") references UserEntity.id)
    val status = enumeration<TaskStatus>("status")

    override val primaryKey = PrimaryKey(id)
}

