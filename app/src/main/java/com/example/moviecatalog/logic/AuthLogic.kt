package com.example.moviecatalog.logic

import com.example.moviecatalog.data.api.AuthApi
import com.example.moviecatalog.data.model.Token
import com.example.moviecatalog.data.model.TokenStorage
import com.example.moviecatalog.data.model.auth.LoginCredentials
import com.example.moviecatalog.data.model.auth.UserRegisterModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthLogic(
    private val authApi: AuthApi,
    private val onClearErrors: () -> Unit = {},
    private val onSuccess: () -> Unit = {},
    private val onError: (String) -> Unit = {}
) {
    fun registerUser(login: String, email: String, name: String, password: String, confirmPassword: String, birthDate: String, gender: Int?) {
        onClearErrors()

        if (login == "") {
            onError("Заполните логин!")
            return
        }

        if (email == "") {
            onError("Заполните почту!")
            return
        }

        if (name == "") {
            onError("Заполните имя!")
            return
        }

        if (password == "") {
            onError("Заполните пароль!")
            return
        }

        if (password.length < 6) {
            onError("Пароль должен содержать хотя бы 6 символов!")
            return
        }

        if (birthDate == "") {
            onError("Заполните дату рождения!")
            return
        }

        if (gender == null) {
            onError("Укажите пол!")
            return
        }

        if (!isValidLogin(login)) {
            onError("Логин может содержать только буквы и цифры")
            return
        }

        if (!isValidEmail(email)) {
            onError("Почта не соответствует формату")
            return
        }

        if (password != confirmPassword) {
            onError("Пароли не совпадают")
            return
        }

        val user = UserRegisterModel(login, name, password, email, birthDate, gender)

        var response: Response<Token>

        CoroutineScope(Dispatchers.IO).launch {
            response = authApi.register(user)

            if (response.isSuccessful) {
                val tokenResponse = response.body()
                val token = tokenResponse?.accessToken ?: ""
                TokenStorage.saveToken(token)

                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }
            else {
                withContext(Dispatchers.Main) {
                    when (response.code()) {
                        400 -> onError("Проверьте правильность заполнения полей или попробуйте другой логин")
                        500 -> onError("Ошибка сервера")
                        else -> onError("Ошибка ${response.code()}")
                    }
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("([A-Za-z]+([0-9]|[A-Za-z])*([.|\\-_]([0-9]|[A-Za-z])*)*)@[A-Za-z]+\\.[A-Za-z]+"))
    }

    private fun isValidLogin(login: String): Boolean {
        return login.matches(Regex("[A-Za-z|0-9]+"))
    }

    fun login(login: String, password: String) {
        onClearErrors()

        if (login == "") {
            onError("Заполните логин!")
            return
        }

        if (password == "") {
            onError("Заполните пароль!")
            return
        }

        val loginCredentials = LoginCredentials(login, password)

        var response: Response<Token>

        CoroutineScope(Dispatchers.IO).launch {
            response = authApi.login(loginCredentials)

            if (response.isSuccessful) {
                val tokenResponse = response.body()
                val token = tokenResponse?.accessToken ?: ""
                TokenStorage.saveToken(token)

                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }
            else {
                withContext(Dispatchers.Main) {
                    when (response.code()) {
                        400 -> onError("Неверные логин или пароль")
                        500 -> onError("Ошибка сервера")
                        else -> onError("Ошибка ${response.code()}")
                    }
                }
            }
        }
    }
}
