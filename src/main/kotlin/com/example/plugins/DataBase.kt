package com.example.plugins

import com.example.data.entity.TaskEntity
import com.example.data.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DataBaseFactory {
    private const val driverClassName = "org.h2.Driver"
    private const val jdbcURL = "jdbc:h2:file:./sqlite3"
    fun init() {
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.create(UserEntity)
            SchemaUtils.create(TaskEntity)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}