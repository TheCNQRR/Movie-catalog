package com.example.moviecatalog.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.moviecatalog.data.model.movie.MovieDetailsModel

class MovieScreenActivity: ComponentActivity() {
    private val effects = Effects()

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

        setContent {
            MovieScreen(movie = movieDetails)
        }
    }
}