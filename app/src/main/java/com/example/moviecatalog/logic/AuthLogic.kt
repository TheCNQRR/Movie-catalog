package com.example.moviecatalog.logic

import android.content.Context
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.AuthApi
import com.example.moviecatalog.data.model.auth.LoginCredentials
import com.example.moviecatalog.data.model.auth.UserRegisterModel
import com.example.moviecatalog.logic.util.TokenManager
import com.example.moviecatalog.logic.util.Validator


class AuthLogic(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
    private val context: Context,
    private val authValidator: Validator = Validator(context),
    private val onClearErrors: () -> Unit = {},
    private val onSuccess: () -> Unit = {},
    private val onError: (String) -> Unit = {}
) {
    suspend fun registerUser(login: String, email: String, name: String, password: String, confirmPassword: String, birthDate: String, gender: Int?) {
        onClearErrors()

        val errorMessage = authValidator.validateRegistration(login, email, name, password, confirmPassword, birthDate, gender)

        if (errorMessage != null) {
            onError(errorMessage)
            return
        }

        val user = UserRegisterModel(login, name, password, email, birthDate, gender!!)

        val response = authApi.register(user)

        if (response.isSuccessful) {
            val tokenResponse = response.body()
            val token = tokenResponse?.accessToken ?: ""
            tokenManager.saveToken(context, token)
            onSuccess()
        }
        else {
            when (response.code()) {
                400 -> onError(context.getString(R.string.check_fields_are_correct))
                500 -> onError(context.getString(R.string.server_error))
                else -> onError(context.getString(R.string.error) + "${response.code()}")
            }
        }
    }

    suspend fun login(login: String, password: String) {
        onClearErrors()

        authValidator.validateLogin(login, password)

        val loginCredentials = LoginCredentials(login, password)

        val response = authApi.login(loginCredentials)

        if (response.isSuccessful) {
            val tokenResponse = response.body()
            val token = tokenResponse?.accessToken ?: ""
            tokenManager.saveToken(context, token)

            onSuccess()
        }
        else {
            when (response.code()) {
                400 -> onError(context.getString(R.string.wrong_login_or_password))
                500 -> onError(context.getString(R.string.server_error))
                else -> onError(context.getString(R.string.error) + "${response.code()}")
            }
        }
    }
}
