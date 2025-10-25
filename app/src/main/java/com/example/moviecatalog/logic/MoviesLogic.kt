package com.example.moviecatalog.logic

import android.content.Context
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.MovieApi;
import com.example.moviecatalog.data.model.movie.MoviesPagedListModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MoviesLogic(
    private val context: Context,
    private val movieApi: MovieApi,
    private val onMoviesLoad: (MoviesPagedListModel) -> Unit,
    private val onError: (String) -> Unit = {}) {
    fun getMovies(page: Int) {
        var response: Response<MoviesPagedListModel>

        CoroutineScope(Dispatchers.IO).launch {
            response = movieApi.getMovies(page)

            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    val movies = response.body()

                    if (movies != null) {
                        onMoviesLoad(movies)
                    }
                    else {
                        onError(context.getString(R.string.empty_answer_from_server))
                    }
                }
            }
            else {
                withContext(Dispatchers.Main) {
                   onError(context.getString(R.string.error))
                }
            }
        }
    }
}
