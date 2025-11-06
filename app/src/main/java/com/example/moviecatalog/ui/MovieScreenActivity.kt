package com.example.moviecatalog.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.moviecatalog.data.api.RetrofitClient
import com.example.moviecatalog.data.model.movie.MovieDetailsModel
import com.example.moviecatalog.data.model.user.ProfileModel
import com.example.moviecatalog.logic.ProfileLogic
import com.example.moviecatalog.logic.util.TokenManager
import kotlinx.coroutines.launch

class MovieScreenActivity: ComponentActivity() {
    private val effects = Effects()
    private val userApi = RetrofitClient.getUserApi()
    private val tokenManager = TokenManager()
    private val authApi = RetrofitClient.getAuthApi()
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
                        MovieScreen(movie = movieDetails, user = user)
                    }
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
        } catch (e: Exception) {
            null
        }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}