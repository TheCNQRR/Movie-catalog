package com.example.moviecatalog.logic

import android.content.Context
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.MovieApi;
import com.example.moviecatalog.data.model.movie.MovieElementModel
import com.example.moviecatalog.data.model.movie.MoviesListModel
import com.example.moviecatalog.data.model.movie.MoviesPagedListModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MoviesLogic(
    private val context: Context,
    private val movieApi: MovieApi,
    private val onMoviesLoaded: (List<MovieElementModel>) -> Unit,
    private val onError: (String?) -> Unit = {}) {
    fun getMovies(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = movieApi.getMovies(page)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val movies = response.body()?.movies ?: emptyList()
                        onMoviesLoaded(movies)
                    } else {
                        onError("Ошибка загрузки: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(context.getString(R.string.error) + " ${e.message}")
                }
            }
        }
    }
}
