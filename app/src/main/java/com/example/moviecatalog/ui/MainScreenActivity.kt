package com.example.moviecatalog.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.RetrofitClient
import com.example.moviecatalog.data.model.movie.MovieElementModel
import com.example.moviecatalog.databinding.MainScreenBinding
import com.example.moviecatalog.logic.MoviesLogic
import com.example.moviecatalog.logic.util.Functions
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.Locale

class MainScreenActivity: AppCompatActivity() {
    private lateinit var binding: MainScreenBinding
    private val effects = Effects()
    private lateinit var moviesLogic: MoviesLogic

    private var currentPage = 1
    private var currentMovies = emptyList<MovieElementModel>()
    private var isLoading = false
    private var hasMorePages = false
    private var isFirstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        binding = MainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        effects.hideSystemBars(window)

        initializeMovieLogic()
        loadFirstPage()
        setupGalleryScrollListener()
    }

    private fun initializeMovieLogic() {
        moviesLogic = MoviesLogic(
            context = this,
            movieApi = RetrofitClient.getMovieApi(),
            onMoviesLoaded = { movies ->
                if (isFirstLoad) {
                    currentMovies = movies
                    isLoading = false
                    hasMorePages = true
                    isFirstLoad = false
                    setUpUI()
                }
                else {
                    if (movies.isNotEmpty()) {
                        currentMovies = currentMovies + movies
                        currentPage++
                        addMoviesToGallery(movies)
                    } else {
                        hasMorePages = false
                    }
                    isLoading = false
                }
            },
            onError = { errorMessage ->
                handleError(errorMessage)
            }
        )
    }

    private fun handleError(errorMessage: String?) {
        isLoading = false
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        println(errorMessage)
    }

    private fun loadFirstPage() {
        if (isLoading) {
            return
        }
        isLoading = true

        lifecycleScope.launch {
            moviesLogic.getMovies(1)
        }
    }

    private fun loadNextPage() {
        if (isLoading || !hasMorePages) {
            return
        }
        isLoading = true

        lifecycleScope.launch {
            moviesLogic.getMovies(currentPage + 1)
        }
    }

    private fun showPoster(promotedMovie: MovieElementModel) {
            Picasso.get()
                .load(promotedMovie.poster)
                .fit()
                .into(binding.moviePoster)
    }

    private fun setUpUI() {
        if (currentMovies.isNotEmpty()) {
            val firstMovie = currentMovies.first()
            showPoster(firstMovie)
            currentMovies = currentMovies.filterIndexed { index, _ -> index != 0 }
            addMoviesToGallery(currentMovies)
        }
        setupFavoriteMovies(emptyList()) //TODO передавать избранное пользователя
    }

    private fun setupFavoriteMovies(favoriteMovies: List<MovieElementModel>) {
        if (favoriteMovies.isEmpty()) {
            binding.favoritesTextInMain.visibility = View.GONE
            binding.horizontalScrollForMovies.visibility = View.GONE

            val parameters = binding.gallery.layoutParams as ConstraintLayout.LayoutParams
            parameters.topToBottom = binding.moviePoster.id
            binding.gallery.layoutParams = parameters

            return
        }

        val container = binding.moviesContainer

        binding.favoritesTextInMain.visibility = View.VISIBLE
        binding.horizontalScrollForMovies.visibility = View.VISIBLE

        val parameters = binding.gallery.layoutParams as ConstraintLayout.LayoutParams
        parameters.topToBottom = binding.horizontalScrollForMovies.id
        binding.gallery.layoutParams = parameters

        favoriteMovies.forEach { movie ->
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
                .setDuration(150)
                .start()
        }
    }

    private fun addMoviesToGallery(movies: List<MovieElementModel>) {
        val container = binding.galleryContainer

        movies.forEach { movie ->
            val movieView = LayoutInflater.from(this).inflate(
                R.layout.movie_item_in_gallery,
                container,
                false
            )

            val poster = movieView.findViewById<ImageView>(R.id.movie_poster)
            val name = movieView.findViewById<TextView>(R.id.movie_name_main_screen)
            val yearCountry = movieView.findViewById<TextView>(R.id.movie_year_country_main_screen)
            val genres = movieView.findViewById<TextView>(R.id.movie_genres_main_screen)
            val rating = movieView.findViewById<TextView>(R.id.movie_rating_main_screen)

            val movieRating = Functions().calculateMovieRating(movie)

            val ratingBackground = when {
                movieRating > 9 && movieRating <= 10 -> R.drawable.rating_masterprice
                movieRating > 8 && movieRating <= 9 -> R.drawable.rating_excellent
                movieRating > 7 && movieRating <= 8 -> R.drawable.rating_very_good
                movieRating > 6 && movieRating <= 7-> R.drawable.rating_good
                movieRating > 5 && movieRating <= 6 -> R.drawable.rating_decent
                movieRating > 4 && movieRating <= 5 -> R.drawable.rating_average
                movieRating > 3 &&  movieRating <= 4 -> R.drawable.rating_below_average
                movieRating > 2 && movieRating <= 3 -> R.drawable.rating_poor
                movieRating > 1 && movieRating <= 2 -> R.drawable.rating_very_poor
                movieRating in 0.0..1.0 -> R.drawable.rating_awful
                else -> R.drawable.rating_gray
            }

            name.isSelected = true
            yearCountry.isSelected = true
            genres.isSelected = true

            Picasso.get().load(movie.poster).into(poster)
            name.text = movie.name
            yearCountry.text = getString(R.string.year_country_format, movie.year, movie.country)
            genres.text = movie.genres.joinToString { it.name }
            rating.text = String.format(Locale.getDefault(), "%.1f", movieRating)
            rating.setBackgroundResource(ratingBackground)

            container.addView(movieView)
        }
    }

    private fun setupGalleryScrollListener() {
        binding.scrollViewMain.viewTreeObserver.addOnScrollChangedListener {
            val scrollView = binding.scrollViewMain

            val galleryContainer = binding.galleryContainer
            val galleryBottom = galleryContainer.bottom

            if (galleryBottom <= scrollView.height + scrollView.scrollY + 500) {
                loadNextPage()
            }
        }
    }
}