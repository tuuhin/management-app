package com.example.domain.facades

import com.example.domain.models.CreateTaskModel
import com.example.domain.models.TaskModel
import com.example.domain.models.UpdateTaskModel
import com.example.domain.models.UserModel

interface TaskDaoFacade {
    suspend fun createTask(model: CreateTaskModel): TaskModel?
    suspend fun updateTask(model: UpdateTaskModel): TaskModel?
    suspend fun deleteTask(model: UserModel, id: Int): Int
    suspend fun getTasks(userModel: UserModel): List<TaskModel?>
    suspend fun getTaskById(id: Int): TaskModel?
}