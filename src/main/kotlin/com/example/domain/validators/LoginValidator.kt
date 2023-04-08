package com.example.domain.validators

import com.example.utils.forms.UserLoginForm

object LoginValidator {

    fun validate(form: UserLoginForm): Validator {
        return if (form.userName.isEmpty() || form.userName.length < 5) Validator(
            isValid = false,
            message = "Username cannot be this short,please provide the correct format"
        )
        else if (form.password.isEmpty() || form.password.length < 8) Validator(
            isValid = false,
            message = "Password should of length of least 8 characters provide the correct one"
        )
        else Validator(isValid = true)
    }
}