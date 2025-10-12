package com.example.moviecatalog.data.api

import com.example.moviecatalog.data.model.movie.MoviesListModel
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteMoviesApi {
    @GET("/api/favorites")
    suspend fun getFavorites(): Response<MoviesListModel>

    @POST("/api/favorites/{id}/add")
    suspend fun addFavorite(@Path("id") id: Long): Response<Unit>

    @DELETE("/api/favorites/{id}/delete")
    suspend fun deleteFavorite(@Path("id") id: Long): Response<Unit>
}