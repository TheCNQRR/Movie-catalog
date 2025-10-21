package com.example.moviecatalog.logic

import com.example.moviecatalog.data.api.RetrofitClient
import com.example.moviecatalog.data.model.Token
import com.example.moviecatalog.data.model.auth.UserRegisterModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthLogic {
    private var token = ""
    private val authApi = RetrofitClient.getAuthApi()

    fun registerUser(login: String, email: String, name: String, password: String, confirmPassword: String, birthDate: String, gender: Int) {
        if (password != confirmPassword) {
            println("Error") //TODO валидация (не только пароля)
        }

        val user = UserRegisterModel(login, name, password, email, birthDate, gender)

        var response: Token

        CoroutineScope(Dispatchers.IO).launch {
            response = authApi.register(user)
            token = response.accessToken
        }
    }
}