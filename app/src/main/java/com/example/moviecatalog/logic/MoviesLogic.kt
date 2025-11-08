package com.example.moviecatalog.logic

import android.content.Context
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.MovieApi
import com.example.moviecatalog.data.model.movie.MovieDetailsModel
import com.example.moviecatalog.data.model.movie.MovieElementModel

class MoviesLogic(
    private val context: Context,
    private val movieApi: MovieApi,
    private val onMoviesLoaded: (List<MovieElementModel>) -> Unit = {},
    private val onMovieDetailsLoaded: (MovieDetailsModel?) -> Unit = {},
    private val onError: (String?) -> Unit = {}
) {
    suspend fun getMovies(page: Int) {
        return try {
            val response = movieApi.getMovies(page)

            if (response.isSuccessful) {
                val movies = response.body()?.movies ?: emptyList()
                onMoviesLoaded(movies)
            } else {
                onError(context.getString(R.string.error) + response.code())
            }
        } catch (e: Exception) {
            onError(context.getString(R.string.error) + e.message)
        }
    }

    suspend fun getMovieDetails(movieId: String) {
        return try {
            val response = movieApi.getMovieDetails(movieId)

            if (response.isSuccessful) {
                val movieDetails = response.body()
                onMovieDetailsLoaded(movieDetails)
            } else {
                onError(context.getString(R.string.error) + response.code())
            }
        } catch (e: Exception) {
            onError(context.getString(R.string.error) + e.message)
        }
    }
}
