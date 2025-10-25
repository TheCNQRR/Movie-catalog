package com.example.moviecatalog.logic.util

import android.content.Context
import com.example.moviecatalog.R

class Validator(private val context: Context) {
    fun validateRegistration(login: String, email: String, name: String, password: String, confirmPassword: String, birthDate: String, gender: Int?): String? {
        val loginError = isValidLogin(login)
        return when {
            loginError != null -> loginError
            email.isBlank() -> context.getString(R.string.fill_email)
            name.isBlank() -> context.getString(R.string.fill_name)
            password.isBlank() -> context.getString(R.string.fill_password)
            password.length < 6 -> context.getString(R.string.password_too_short)
            birthDate.isBlank() -> context.getString(R.string.fill_birth_date)
            gender == null -> context.getString(R.string.select_gender)
            !isValidEmail(email) -> context.getString(R.string.incorrect_email)
            password != confirmPassword -> context.getString(R.string.passwords_not_match)
            else -> null
        }
    }

    fun validateLogin(login: String, password: String): String? {
        val passwordError = isValidPassword(password)
        return when {
            login.isBlank() -> context.getString(R.string.fill_login)
            passwordError != null -> passwordError
            else -> null
        }
    }

    private fun isValidLogin(login: String): String? {
        return when {
            login.isBlank() -> context.getString(R.string.fill_login)
            !login.matches(Regex("[A-Za-z|0-9]+")) -> context.getString(R.string.incorrect_login)
            else -> null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("([A-Za-z]+([0-9]|[A-Za-z])*([.|\\-_]([0-9]|[A-Za-z])*)*)@[A-Za-z]+\\.[A-Za-z]+"))
    }

    private fun isValidPassword(password: String): String? {
        return when {
            password.isBlank() -> context.getString(R.string.fill_password)
            password.length < 6 -> context.getString(R.string.password_too_short)
            else -> null
        }
    }
}