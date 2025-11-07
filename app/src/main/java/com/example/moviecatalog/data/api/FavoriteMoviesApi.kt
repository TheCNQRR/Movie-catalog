package com.example.moviecatalog.data.api

import com.example.moviecatalog.data.model.movie.MoviesListModel
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteMoviesApi {
    @Headers("Content-type: application/json")
    @GET("api/favorites")
    suspend fun getFavorites(@Header("Authorization") token: String): Response<MoviesListModel>

    @Headers("Content-type: application/json")
    @POST("api/favorites/{id}/add")
    suspend fun addFavorite(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>

    @Headers("Content-type: application/json")
    @DELETE("api/favorites/{id}/delete")
    suspend fun deleteFavorite(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>
}
