package com.example.moviecatalog.data.api

import com.example.moviecatalog.data.model.user.ProfileModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT

interface UserApi {
    @Headers("Content-type: application/json")
    @GET("api/account/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ProfileModel>

    @Headers("Content-type: application/json")
    @PUT("api/account/profile")
    suspend fun editProfile(@Header("Authorization") token: String, @Body body: ProfileModel): Response<Unit>
}
