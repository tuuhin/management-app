package com.example.domain.validators

import com.example.utils.Validator
import com.example.utils.forms.SignUpForm

object SignUpValidator {
    fun validate(form: SignUpForm): Validator {
        return if (form.userName.isEmpty() && form.userName.length < 5) Validator(isValid = false, message = "")
        else if (form.password1 != form.password2) Validator(
            isValid = false,
            message = "Password aren't same please put same passwords"
        ) else if (form.password1.length < 8) Validator(
            isValid = false,
            message = "Passwords should be of least of length 8 character"
        )
        else Validator(isValid = true)
    }
}