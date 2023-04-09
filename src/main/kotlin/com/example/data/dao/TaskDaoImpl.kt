package com.example.data.dao

import com.example.data.entity.TaskEntity
import com.example.data.entity.UserEntity
import com.example.domain.facades.TaskDaoFacade
import com.example.domain.models.*
import com.example.plugins.DataBaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class TaskDaoImpl : TaskDaoFacade {

    private fun rowsToTask(row: ResultRow): TaskModel =
        TaskModel(
            id = row[TaskEntity.id],
            title = row[TaskEntity.title],
            desc = row[TaskEntity.desc],
            createdAt = row[TaskEntity.createdAt],
            updatedAt = row[TaskEntity.updatedAt],
            status = row[TaskEntity.status]
        )

    override suspend fun createTask(model: CreateTaskModel): TaskModel? {
       return dbQuery {
            TaskEntity.insert {
                it[title] = model.title
                it[desc] = model.desc
                it[createdAt] = LocalDateTime.now()
                it[updatedAt] = LocalDateTime.now()
                it[user] = model.user.id
                it[status] = model.status
            }.resultedValues?.singleOrNull()?.let(::rowsToTask)
        }
    }

    override suspend fun updateTask(model: UpdateTaskModel): TaskModel? {
        return dbQuery {
            TaskEntity.update({ TaskEntity.id eq model.id }) {
                it[title] = model.title
                it[desc] = model.desc
                it[status] = model.status
                it[updatedAt] = model.updatedAt
            }
            TaskEntity.select {
                TaskEntity.id eq model.id
            }.singleOrNull()
        }?.let(::rowsToTask)
    }

    override suspend fun deleteTask(model: UserModel, id: Int) = dbQuery {
        TaskEntity.deleteWhere { (TaskEntity.id eq id) and (user eq model.id) }
    }

    override suspend fun getTasks(userModel: UserModel): List<TaskModel?> {
       return dbQuery {
            (TaskEntity innerJoin UserEntity).select {
                TaskEntity.user eq UserEntity.id
            }.map(::rowsToTask)
        }
    }

    override suspend fun getTaskById(id: Int): TaskModel? {
        return  dbQuery {
            TaskEntity.select {
                TaskEntity.id eq id
            }.singleOrNull()?.let(::rowsToTask)
        }
    }
}