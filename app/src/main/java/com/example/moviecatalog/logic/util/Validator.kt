package com.example.moviecatalog.logic.util

import android.content.Context

class Validator() {
    fun validateRegistration(login: String, email: String, name: String, password: String, confirmPassword: String, birthDate: String, gender: Int?): String? {
        val loginError = isValidLogin(login)
        return when {
            loginError != null -> loginError
            email.isBlank() -> "Заполните почту!"
            name.isBlank() -> "Заполните имя!"
            password.isBlank() -> "Заполните пароль!"
            password.length < 6 -> "Пароль должен содержать хотя бы 6 символов!"
            birthDate.isBlank() -> "Заполните дату рождения!"
            gender == null -> "Укажите пол!"
            !isValidEmail(email) -> "Почта не соответствует формату"
            password != confirmPassword -> "Пароли не совпадают"
            else -> null
        }
    }

    fun validateLogin(login: String, password: String): String? {
        val passwordError = isValidPassword(password)
        return when {
            login.isBlank() -> "Заполните логин!"
            passwordError != null -> passwordError
            else -> null
        }
    }

    private fun isValidLogin(login: String): String? {
        return when {
            login.isBlank() -> "Заполните логин!"
            !login.matches(Regex("[A-Za-z|0-9]+")) -> "Логин может содержать только буквы и цифры"
            else -> null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("([A-Za-z]+([0-9]|[A-Za-z])*([.|\\-_]([0-9]|[A-Za-z])*)*)@[A-Za-z]+\\.[A-Za-z]+"))
    }

    private fun isValidPassword(password: String): String? {
        return when {
            password.isBlank() -> "Заполните пароль!"
            password.length < 6 -> "Пароль должен содержать хотя бы 6 символов!"
            else -> null
        }
    }
}