package com.example.data.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object UserEntity : Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 50).nullable()
    val password = binary("password",100)
    val username = varchar("username", 100).uniqueIndex()
    val createdAt = date("created_at")
    override val primaryKey = PrimaryKey(id)
}