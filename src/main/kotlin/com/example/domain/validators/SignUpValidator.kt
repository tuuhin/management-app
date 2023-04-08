package com.example.domain.validators

import com.example.utils.forms.SignUpForm

object SignUpValidator {

    private val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
    fun validate(form: SignUpForm): Validator {
        return if (form.userName.isEmpty() && form.userName.length < 5) Validator(
            isValid = false,
            message = "Username should be least of 5 characters long"
        )
        else if (form.password1 != form.password2) Validator(
            isValid = false,
            message = "Password aren't same please put same passwords"
        ) else if (form.password1.length < 8) Validator(
            isValid = false,
            message = "Passwords should be of least of length 8 character"
        ) else if (!emailRegex.matches(form.email)) Validator(
            isValid = false,
            message = "Invalid email format ,please provide the correct format"
        )
        else Validator(isValid = true)
    }
}