package com.example.moviecatalog.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.RetrofitClient
import com.example.moviecatalog.data.model.movie.MovieDetailsModel
import com.example.moviecatalog.data.model.review.ReviewModifyModel
import com.example.moviecatalog.data.model.user.ProfileModel
import com.example.moviecatalog.logic.ProfileLogic
import com.example.moviecatalog.logic.util.TokenManager
import kotlinx.coroutines.launch

class MovieScreenActivity: ComponentActivity() {
    private val effects = Effects()
    private val userApi = RetrofitClient.getUserApi()
    private val tokenManager = TokenManager()
    private val retrofitClient = RetrofitClient
    private val authApi = retrofitClient.getAuthApi()
    private val reviewApi = retrofitClient.getReviewApi()
    private val movieApi = retrofitClient.getMovieApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        effects.hideSystemBars(window)

        val movieDetails = intent.getParcelableExtra<MovieDetailsModel>("movie_details")

        if (movieDetails == null) {
            finish()
            return
        }

        val token = tokenManager.getToken(this)

        if (token != null) {
            lifecycleScope.launch {
                val user = loadUserProfile(token)
                if (user != null) {
                    setContent {
                        MovieScreen(movie = movieDetails, user = user, onBackButtonClick = { onBackButtonClick() }, onAddReview = { movieId, rating, reviewText, isAnonymous -> onAddReview(movieId, rating, reviewText, isAnonymous) })
                    }
                }
            }
        }
        else {
            navigateToSignIn()
        }
    }

    private fun onBackButtonClick() {
        finish()
    }

    private fun onAddReview(movieId: String, rating: Int, reviewText: String, isAnonymous: Boolean) {
        val token = tokenManager.getToken(this)

        val reviewBody = ReviewModifyModel(reviewText, rating, isAnonymous)

        if (token != null) {
            lifecycleScope.launch {
                try {
                    val response = reviewApi.addReview("Bearer ${token}", movieId, reviewBody)

                    if (response.isSuccessful) {
                        val movieResponse = movieApi.getMovieDetails(movieId)

                        runOnUiThread {
                            val updatedMovie = movieResponse.body()

                            val intent = Intent(this@MovieScreenActivity, MovieScreenActivity::class.java).apply {
                                putExtra("movie_details", updatedMovie)
                            }
                            finish()
                            startActivity(intent)
                        }
                    }
                    else {
                        when (response.code()) {
                            401 -> {
                                navigateToSignIn()
                                tokenManager.clearToken(this@MovieScreenActivity)
                            }
                            else -> Toast.makeText(this@MovieScreenActivity, getString(R.string.error) + " " + response.code(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                catch (e: Exception) {
                    println(e.message)
                    Toast.makeText(this@MovieScreenActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            navigateToSignIn()
        }
    }

    private suspend fun loadUserProfile(token: String): ProfileModel? {
        return try {
            val profileLogic = ProfileLogic(userApi, authApi, token, this, onLogout = {
                navigateToSignIn()
            })
            profileLogic.getUser()
        }
        catch (e: Exception) {
            null
        }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}