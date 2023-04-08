package com.example.domain.facades

import com.example.domain.models.CreateTaskModel
import com.example.domain.models.TaskModel
import com.example.domain.models.UserModel

interface TaskDaoFacade {
    suspend fun createTask(model: CreateTaskModel): TaskModel?
    suspend fun updateTask(model: TaskModel): TaskModel?
    suspend fun deleteTask(model: TaskModel): Int
    suspend fun getTasks(userModel: UserModel): List<TaskModel?>
}