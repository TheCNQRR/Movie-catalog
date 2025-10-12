package com.example.moviecatalog.data.api

import com.example.moviecatalog.data.model.user.ProfileModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {
    @GET("/api/account/profile")
    suspend fun getProfile(): Response<ProfileModel>

    @PUT("/api/account/profile")
    suspend fun editProfile(@Body body: ProfileModel): Response<Unit>
}