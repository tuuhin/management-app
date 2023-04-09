package com.example.utils.forms

import com.example.domain.models.CreateTaskModel
import com.example.domain.models.TaskStatus
import com.example.domain.models.UserModel
import io.ktor.http.*

data class CreateTaskForm(
    val title: String,
    val desc: String? = null,
    val status: TaskStatus
) {
    fun toModel(user: UserModel): CreateTaskModel {
        return CreateTaskModel(
            title = title,
            desc = desc,
            status = status,
            user = user
        )
    }

    companion object {
        fun fromParameters(params: Parameters): CreateTaskForm {
            val status = when (params["status"]) {
                "NO_STATUS" -> TaskStatus.NO_STATUS
                "WORKING" -> TaskStatus.WORKING
                "TWEAKING" -> TaskStatus.TWEAKING
                "COMPLETED" -> TaskStatus.COMPLETED
                else -> TaskStatus.NO_STATUS
            }

            return CreateTaskForm(
                title = params["title"] ?: "",
                desc = if (params["description"]?.isNotEmpty() == true) params["description"] else null,
                status = status
            )
        }
    }
}
