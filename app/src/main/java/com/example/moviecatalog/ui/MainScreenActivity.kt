package com.example.moviecatalog.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.RetrofitClient
import com.example.moviecatalog.data.model.movie.MovieElementModel
import com.example.moviecatalog.databinding.MainScreenBinding
import com.example.moviecatalog.logic.MoviesLogic
import com.example.moviecatalog.logic.util.TokenManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class MainScreenActivity : AppCompatActivity() {
    companion object {
        private const val NORMAL_SCALE = 1f
        private const val SMALL_SCALE = 0.83f
        private const val PADDING = 500
        private const val ZERO = 0
        private const val DURATION = 150L
        private const val HTTP_UNAUTHORIZED = 401
    }
    private lateinit var binding: MainScreenBinding
    private val effects = Effects()
    private lateinit var moviesLogic: MoviesLogic
    private val tokenManager = TokenManager()
    private val retrofitClient = RetrofitClient
    private val favoriteMoviesApi = retrofitClient.getFavoritesApi()
    private var favoriteMovies: List<MovieElementModel> = emptyList()

    private val galleryAdapter = GalleryAdapter { movie ->
        lifecycleScope.launch {
            val moviesLogic = MoviesLogic(
                context = this@MainScreenActivity,
                movieApi = RetrofitClient.getMovieApi(),
                onMoviesLoaded = {},
                onMovieDetailsLoaded = { details ->
                    runOnUiThread {
                        val intent = Intent(this@MainScreenActivity, MovieScreenActivity::class.java).apply {
                            putExtra(getString(R.string.movie_details), details)
                        }
                        startActivity(intent)
                    }
                },
                onError = { _ ->
                    runOnUiThread {
                        Toast.makeText(
                            this@MainScreenActivity,
                            getString(R.string.load_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
            moviesLogic.getMovieDetails(movie.id)
        }
    }

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

        val token = tokenManager.getToken(this)
        if (token == null) {
            navigateToSignIn()
            return
        }

        setupRecyclerView()
        initializeMovieLogic()
        loadFirstPage()
        setupGalleryScrollListener()
        setupProfileClickListener()
        setupCollectionClickListener()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            loadFavorites()
        }
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
                } else {
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
            currentMovies = currentMovies.filterIndexed { index, _ -> index != ZERO }
            addMoviesToGallery(currentMovies)
            setupOnPromotedMovieClickListener(firstMovie)
        }
        lifecycleScope.launch {
            loadFavorites()
        }
    }

    private fun setupFavoriteMovies(favoriteMovies: List<MovieElementModel>) {
        val container = binding.moviesContainer

        container.removeAllViews()

        if (favoriteMovies.isEmpty()) {
            binding.favoritesTextInMain.visibility = View.GONE
            binding.horizontalScrollForMovies.visibility = View.GONE

            val parameters = binding.gallery.layoutParams as ConstraintLayout.LayoutParams
            parameters.topToBottom = binding.moviePoster.id
            binding.gallery.layoutParams = parameters

            return
        }

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
            val deleteIcon = movieView.findViewById<ImageView>(R.id.delete_icon)

            cardView.scaleX = NORMAL_SCALE
            cardView.scaleY = NORMAL_SCALE

            Picasso.get().load(movie.poster).into(poster)

            setupFavoriteMovieClickListener(movie, cardView)

            setupDeleteClickListener(movie, deleteIcon, container, movieView)

            container.addView(movieView)
        }

        binding.horizontalScrollForMovies.post {
            updateCenterItem()
        }

        binding.horizontalScrollForMovies.viewTreeObserver.addOnScrollChangedListener {
            updateCenterItem()
        }
    }

    private fun setupDeleteClickListener(
        movie: MovieElementModel,
        deleteIcon: ImageView,
        container: View,
        movieView: View
    ) {
        deleteIcon.setOnClickListener {
            deleteFromFavorites(movie, container, movieView)
        }
    }

    private fun deleteFromFavorites(
        movie: MovieElementModel,
        container: View,
        movieView: View
    ) {
        val token = tokenManager.getToken(this)
        if (token != null) {
            lifecycleScope.launch {
                try {
                    // ОТПРАВЛЯЕМ API ЗАПРОС НА УДАЛЕНИЕ
                    val response = favoriteMoviesApi.deleteFavorite(
                        getString(R.string.bearer) + " " + token,
                        movie.id
                    )

                    if (response.isSuccessful) {
                        runOnUiThread {
                            (container as? ViewGroup)?.removeView(movieView)

                            favoriteMovies = favoriteMovies.filter { it.id != movie.id }

                            if (favoriteMovies.isEmpty()) {
                                setupFavoriteMovies(emptyList())
                            } else {
                                updateCenterItem()
                            }
                        }
                    } else {
                        runOnUiThread {
                            when (response.code()) {
                                HTTP_UNAUTHORIZED -> {
                                    navigateToSignIn()
                                    tokenManager.clearToken(this@MainScreenActivity)
                                }
                                else -> Toast.makeText(
                                    this@MainScreenActivity,
                                    getString(R.string.error) + " " + response.code(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainScreenActivity,
                            getString(R.string.error) + " " + e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            navigateToSignIn()
        }
    }

    private fun setupFavoriteMovieClickListener(movie: MovieElementModel, cardView: CardView) {
        cardView.setOnClickListener {
            lifecycleScope.launch {
                val moviesLogic = MoviesLogic(
                    context = this@MainScreenActivity,
                    movieApi = RetrofitClient.getMovieApi(),
                    onMoviesLoaded = {},
                    onMovieDetailsLoaded = { details ->
                        runOnUiThread {
                            val intent = Intent(this@MainScreenActivity, MovieScreenActivity::class.java).apply {
                                putExtra(getString(R.string.movie_details), details)
                            }
                            startActivity(intent)
                        }
                    },
                    onError = { _ ->
                        runOnUiThread {
                            Toast.makeText(
                                this@MainScreenActivity,
                                getString(R.string.load_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
                moviesLogic.getMovieDetails(movie.id)
            }
        }
    }

    private fun updateCenterItem() {
        val scrollView = binding.horizontalScrollForMovies
        val container = binding.moviesContainer

        var firstVisiblePosition = ZERO
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

        for (i in ZERO until container.childCount) {
            val cardView = container.getChildAt(i) as CardView
            val scale = if (i == firstVisiblePosition) NORMAL_SCALE else SMALL_SCALE

            cardView.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(DURATION)
                .start()
        }
    }

    private fun setupRecyclerView() {
        binding.galleryContainer.apply {
            layoutManager = LinearLayoutManager(this@MainScreenActivity)
            adapter = galleryAdapter
        }
    }

    private fun addMoviesToGallery(movies: List<MovieElementModel>) {
        galleryAdapter.addMovies(movies)
    }

    private fun setupGalleryScrollListener() {
        binding.scrollViewMain.viewTreeObserver.addOnScrollChangedListener {
            val scrollView = binding.scrollViewMain

            val galleryContainer = binding.galleryContainer
            val galleryBottom = galleryContainer.bottom

            if (galleryBottom <= scrollView.height + scrollView.scrollY + PADDING) {
                loadNextPage()
            }
        }
    }

    private fun setupProfileClickListener() {
        binding.mainScreenProfile.setOnClickListener {
            startProfileScreen()
        }
        binding.profileTextInMainScreen.setOnClickListener {
            startProfileScreen()
        }
    }

    private fun setupCollectionClickListener() {
        binding.starTextInMainScreen.setOnClickListener {
            startCollectionsScreen()
        }
        binding.mainScreenStar.setOnClickListener {
            startCollectionsScreen()
        }
    }

    private fun setupOnPromotedMovieClickListener(movie: MovieElementModel) {
        binding.watchPromoted.setOnClickListener {
            if (currentMovies.isNotEmpty()) {
                lifecycleScope.launch {
                    val moviesLogic = MoviesLogic(
                        context = this@MainScreenActivity,
                        movieApi = RetrofitClient.getMovieApi(),
                        onMoviesLoaded = {},
                        onMovieDetailsLoaded = { details ->
                            runOnUiThread {
                                val intent = Intent(this@MainScreenActivity, MovieScreenActivity::class.java).apply {
                                    putExtra(getString(R.string.movie_details), details)
                                }
                                startActivity(intent)
                            }
                        },
                        onError = { _ ->
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainScreenActivity,
                                    getString(R.string.load_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                    moviesLogic.getMovieDetails(movie.id)
                }
            }
        }
    }

    private suspend fun loadFavorites() {
        val token = tokenManager.getToken(this)
        if (token != null) {
            try {
                val response = favoriteMoviesApi.getFavorites(getString(R.string.bearer) + " " + token)
                if (response.isSuccessful) {
                    favoriteMovies = response.body()!!.movies
                    runOnUiThread {
                        setupFavoriteMovies(favoriteMovies)
                    }
                } else {
                    when (response.code()) {
                        HTTP_UNAUTHORIZED -> {
                            navigateToSignIn()
                            tokenManager.clearToken(this@MainScreenActivity)
                        }
                        else -> {
                            favoriteMovies = emptyList()
                        }
                    }
                }
            } catch (e: Exception) {
                favoriteMovies = emptyList()
            }
        } else {
            navigateToSignIn()
        }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startProfileScreen() {
        val intent = Intent(this, ProfileScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startCollectionsScreen() {
        val intent = Intent(this, CollectionScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}
