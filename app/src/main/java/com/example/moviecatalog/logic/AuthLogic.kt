package com.example.moviecatalog.logic

import android.content.Context
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.AuthApi
import com.example.moviecatalog.data.model.Token
import com.example.moviecatalog.data.model.TokenStorage
import com.example.moviecatalog.data.model.auth.LoginCredentials
import com.example.moviecatalog.data.model.auth.UserRegisterModel
import com.example.moviecatalog.logic.util.Validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthLogic(
    private val authApi: AuthApi,
    private val context: Context,
    private val authValidator: Validator = Validator(context),
    private val onClearErrors: () -> Unit = {},
    private val onSuccess: () -> Unit = {},
    private val onError: (String) -> Unit = {}
) {
    fun registerUser(login: String, email: String, name: String, password: String, confirmPassword: String, birthDate: String, gender: Int?) {
        onClearErrors()

        val errorMessage = authValidator.validateRegistration(login, email, name, password, confirmPassword, birthDate, gender)

        if (errorMessage != null) {
            onError(errorMessage)
            return
        }

        val user = UserRegisterModel(login, name, password, email, birthDate, gender!!)

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
                        400 -> onError(context.getString(R.string.check_fields_are_correct))
                        500 -> onError(context.getString(R.string.server_error))
                        else -> onError(context.getString(R.string.error) + "${response.code()}")
                    }
                }
            }
        }
    }

    fun login(login: String, password: String) {
        onClearErrors()

        authValidator.validateLogin(login, password)

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
                        400 -> onError(context.getString(R.string.wrong_login_or_password))
                        500 -> onError(context.getString(R.string.server_error))
                        else -> onError(context.getString(R.string.error) + "${response.code()}")
                    }
                }
            }
        }
    }
}
