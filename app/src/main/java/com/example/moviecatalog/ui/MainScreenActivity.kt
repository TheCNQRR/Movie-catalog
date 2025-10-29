package com.example.moviecatalog.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviecatalog.R
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
                .fit()
                .into(binding.moviePoster)

            setupHorizontalList(movies)
        }
    }

    private fun setupHorizontalList(movies: List<MovieElementModel>) {
        val container = binding.moviesContainer

        movies.forEach { movie ->
            val movieView = LayoutInflater.from(this).inflate(
                R.layout.movie_item_in_favorites,
                container,
                false
            )

            val cardView = movieView.findViewById<CardView>(R.id.card_view)
            val poster = movieView.findViewById<ImageView>(R.id.movie_poster)

            cardView.scaleX = 1f
            cardView.scaleY = 1f

            Picasso.get().load(movie.poster).into(poster)

            container.addView(movieView)
        }

        binding.horizontalScrollForMovies.post {
            updateCenterItem()
        }

        binding.horizontalScrollForMovies.viewTreeObserver.addOnScrollChangedListener {
            updateCenterItem()
        }
    }

    private fun updateCenterItem() {
        val scrollView = binding.horizontalScrollForMovies
        val container = binding.moviesContainer

        var firstVisiblePosition = 0
        var minLeft = Int.MAX_VALUE

        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)

            if (child.right > scrollView.scrollX && child.left < scrollView.scrollX + scrollView.width) {
                if (child.left < minLeft) {
                    minLeft = child.left
                    firstVisiblePosition = i
                }
            }
        }

        for (i in 0 until container.childCount) {
            val cardView = container.getChildAt(i) as CardView
            val scale = if (i == firstVisiblePosition) 1f else 0.83f

            cardView.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(50)
                .start()
        }
    }
}