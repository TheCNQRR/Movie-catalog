package com.example.moviecatalog.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.moviecatalog.logic.util.TokenManager

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager()

        val destinationActivity = if (tokenManager.getToken(this) != null) {
            MainScreenActivity::class.java
        } else {
            SignInActivity::class.java
        }

        startActivity(Intent(this, destinationActivity))
        finish()
    }
}
