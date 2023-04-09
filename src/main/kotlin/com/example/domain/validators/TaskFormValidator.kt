package com.example.domain.validators

import com.example.utils.forms.CreateTaskForm

object TaskFormValidator {

    fun execute(form: CreateTaskForm): Validator {
        return if (form.title.isEmpty() || form.title.length < 5) Validator(
            isValid = false,
            message = "Cannot create such a small title, least 5 characters required to create a task"
        ) else Validator(isValid = true)
    }
}