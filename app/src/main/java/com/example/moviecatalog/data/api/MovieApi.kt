package com.example.moviecatalog.data.api

import com.example.moviecatalog.data.model.movie.MovieDetailsModel
import com.example.moviecatalog.data.model.movie.MoviesPagedListModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {
    @GET("api/movies/{page}")
    suspend fun getMovies(@Path("page") page: Int): Response<MoviesPagedListModel>

    @GET("api/movies/details/{id}")
    suspend fun getMovieDetails(@Path("id") id: String): Response<MovieDetailsModel>
}
