package com.example.data.dao

import com.example.data.entity.TaskEntity
import com.example.data.entity.UserEntity
import com.example.domain.facades.TaskDaoFacade
import com.example.domain.models.CreateTaskModel
import com.example.domain.models.TaskModel
import com.example.domain.models.TaskStatus
import com.example.domain.models.UserModel
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
        val create = dbQuery {
            TaskEntity.insert {
                it[title] = model.title
                it[desc] = model.desc
                it[createdAt] = LocalDateTime.now()
                it[updatedAt] = LocalDateTime.now()
                it[user] = model.user.id
                it[status] = TaskStatus.NO_STATUS
            }.resultedValues?.singleOrNull()?.let(::rowsToTask)
        }
        return create
    }

    override suspend fun updateTask(model: TaskModel): TaskModel? {
        val task = dbQuery {
            TaskEntity.update({ TaskEntity.id eq model.id }) {
                it[title] = model.title
                it[desc] = model.desc
                it[status] = model.status
                it[updatedAt] = LocalDateTime.now()
            }
            TaskEntity.select {
                TaskEntity.id eq model.id
            }.singleOrNull()
        }?.let(::rowsToTask)
        return task
    }

    override suspend fun deleteTask(model: TaskModel) = dbQuery {
        TaskEntity.deleteWhere { id eq model.id }
    }

    override suspend fun getTasks(userModel: UserModel): List<TaskModel?> {
        val tasks = dbQuery {
            (TaskEntity innerJoin UserEntity).select {
                TaskEntity.user eq UserEntity.id
            }.map(::rowsToTask)
        }
        return tasks
    }
}