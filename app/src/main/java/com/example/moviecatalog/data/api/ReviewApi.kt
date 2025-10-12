package com.example.moviecatalog.data.api

import com.example.moviecatalog.data.model.review.ReviewModifyModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewApi {
    @POST("/api/movie/{movieId}/review/add")
    suspend fun addReview(@Path("movieId") movieId: String, @Body body: ReviewModifyModel): Response<Unit>

    @PUT("/api/movie/{movieId}/review/{id}/edit")
    suspend fun editReview(@Path("movieId") movieId: String, @Path("id") id: String, @Body body: ReviewModifyModel): Response<Unit>

    @DELETE("/api/movie/{movieId}/review/{id}/delete")
    suspend fun deleteReview(@Path("movieId") movieId: String, @Path("id") id: String): Response<Unit>
}