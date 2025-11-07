package com.example.moviecatalog.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.moviecatalog.data.api.RetrofitClient
import com.example.moviecatalog.data.model.user.ProfileModel
import com.example.moviecatalog.databinding.ProfileScreenBinding
import com.example.moviecatalog.logic.AuthLogic
import com.example.moviecatalog.logic.util.TokenManager
import kotlinx.coroutines.launch

class ProfileScreenActivity: AppCompatActivity() {
    private lateinit var binding: ProfileScreenBinding
    private val effects = Effects()
    private val tokenManager = TokenManager()
    private val retrofitClient = RetrofitClient
    private val profileApi = retrofitClient.getUserApi()
    private val authApi = retrofitClient.getAuthApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        binding = ProfileScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        effects.hideSystemBars(window)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.logout.setOnClickListener {
            effects.onButtonClick(binding.logout)
            tokenManager.clearToken(this)

            lifecycleScope.launch {
                authApi.logout()
            }

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        val user: ProfileModel
    }
}