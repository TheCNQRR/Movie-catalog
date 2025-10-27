package com.example.moviecatalog.ui

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviecatalog.data.api.RetrofitClient
import com.example.moviecatalog.data.model.movie.MovieElementModel
import com.example.moviecatalog.databinding.MainScreenBinding
import com.example.moviecatalog.logic.MoviesLogic
import com.squareup.picasso.Picasso

class MainScreenActivity: AppCompatActivity() {
    private lateinit var binding: MainScreenBinding
    private val effects = Effects()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        binding = MainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        effects.hideSystemBars(window)

        loadMovies()
    }

    private fun loadMovies() {
        val movieLogic = MoviesLogic(
            context = this,
            movieApi = RetrofitClient.getMovieApi(),
            onMoviesLoaded = { movies ->  showPoster(movies) },
            onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            println(errorMessage)}
        )

        movieLogic.getMovies(1)
    }

    private fun showPoster(movies: List<MovieElementModel>) {
        if (movies.isNotEmpty()) {
            val firstMovie = movies[0]

            Picasso.get()
                .load(firstMovie.poster)
                .into(binding.moviePoster)
        }
    }
}