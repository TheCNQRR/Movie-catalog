package com.example.moviecatalog

import android.app.Application
import com.example.moviecatalog.logic.util.TokenManager

class MovieCatalogApplication : Application() {
    companion object {
        val tokenManager = TokenManager()
    }
}