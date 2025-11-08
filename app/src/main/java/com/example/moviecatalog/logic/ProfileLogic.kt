package com.example.moviecatalog.logic

import android.content.Context
import android.widget.Toast
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.AuthApi
import com.example.moviecatalog.data.api.UserApi
import com.example.moviecatalog.data.model.user.ProfileModel

class ProfileLogic(
    private val userApi: UserApi,
    private val authApi: AuthApi,
    private val token: String,
    private val context: Context,
    private val onLogout: () -> Unit = {}
) {
    companion object {
        private const val HTTP_UNAUTHORIZED = 401
    }
    suspend fun getUser(): ProfileModel? {
        val response = userApi.getProfile(context.getString(R.string.bearer) + " " + token)

        return if (response.isSuccessful) {
            response.body()
        } else {
            when (response.code()) {
                HTTP_UNAUTHORIZED -> {
                    try {
                        authApi.logout()
                    } catch (e: Exception) {
                        println(e.message)
                    }
                    onLogout()
                    null
                }
                else -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error) + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                    null
                }
            }
        }
    }
}
