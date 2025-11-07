package com.example.moviecatalog.data.api

import com.example.moviecatalog.data.model.review.ReviewModifyModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewApi {
    @Headers("Content-type: application/json")
    @POST("api/movie/{movieId}/review/add")
    suspend fun addReview(@Header(
        "Authorization"
    ) token: String, @Path("movieId") movieId: String, @Body body: ReviewModifyModel): Response<Unit>

    @Headers("Content-type: application/json")
    @PUT("api/movie/{movieId}/review/{id}/edit")
    suspend fun editReview(
        @Header("Authorization") token: String,
        @Path("movieId") movieId: String,
        @Path("id") id: String,
        @Body body: ReviewModifyModel
    ): Response<Unit>

    @Headers("Content-type: application/json")
    @DELETE("api/movie/{movieId}/review/{id}/delete")
    suspend fun deleteReview(@Header(
        "Authorization"
    ) token: String, @Path("movieId") movieId: String, @Path("id") id: String): Response<Unit>
}
