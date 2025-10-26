package com.example.moviecatalog.data.api

import com.example.moviecatalog.data.model.LogoutResponse
import com.example.moviecatalog.data.model.Token
import com.example.moviecatalog.data.model.auth.LoginCredentials
import com.example.moviecatalog.data.model.auth.UserRegisterModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/account/register")
    suspend fun register(@Body body: UserRegisterModel): Response<Token>

    @POST("api/account/login")
    suspend fun login(@Body body: LoginCredentials): Response<Token>

    @POST("api/account/logout")
    suspend fun logout(): LogoutResponse
}