package com.example.moviecatalog.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.RetrofitClient
import com.example.moviecatalog.data.model.user.ProfileModel
import com.example.moviecatalog.databinding.ProfileScreenBinding
import com.example.moviecatalog.logic.util.Functions
import com.example.moviecatalog.logic.util.TokenManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ProfileScreenActivity : AppCompatActivity() {
    private lateinit var binding: ProfileScreenBinding
    private val effects = Effects()
    private val tokenManager = TokenManager()
    private val retrofitClient = RetrofitClient
    private val profileApi = retrofitClient.getUserApi()
    private val authApi = retrofitClient.getAuthApi()

    companion object {
        private const val HTTP_UNAUTHORIZED = 401
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setupWindow()
        setupBinding()
        setupWindowInsest()
        bindingSetOnCliclListener(binding)
        navigateToMainScreenListener()
        navigateToCollectionScreenListener()

        var user: ProfileModel

        val token = tokenManager.getToken(this)

        if (token != null) {
            lifecycleScope.launch {
                try {
                    val response = profileApi.getProfile(getString(R.string.bearer) + " " + token)

                    if (response.isSuccessful) {
                        user = response.body()!!
                        fillUserInfo(user)
                    } else {
                        when (response.code()) {
                            HTTP_UNAUTHORIZED -> {
                                navigateToSignIn()
                                tokenManager.clearToken(this@ProfileScreenActivity)
                            }
                            else -> {
                                Toast.makeText(this@ProfileScreenActivity, getString(R.string.error) + " " + response.code(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@ProfileScreenActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupWindow() {
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        effects.hideSystemBars(window)
    }

    private fun setupBinding() {
        binding = ProfileScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupWindowInsest() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun bindingSetOnCliclListener(binding: ProfileScreenBinding) {
        binding.logout.setOnClickListener {
            effects.onButtonClick(binding.logout)
            tokenManager.clearToken(this)

            lifecycleScope.launch {
                try {
                    authApi.logout()
                } catch (e: Exception) {
                    Toast.makeText(this@ProfileScreenActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            navigateToSignIn()
        }
    }

    private fun fillUserInfo(user: ProfileModel) {
        if (!user.avatarLink.isNullOrBlank() && user.avatarLink != "") {
            Picasso.get().load(user.avatarLink).into(binding.avatarProfile)
        } else {
            binding.avatarProfile.setImageResource(R.drawable.default_user_icon)
        }
        binding.userNameProfile.text = user.nickName
        binding.userEmail.setText(user.email)
        binding.userName.setText(user.name)
        binding.userBirthDate.setText(Functions().formatDate(user.birthDate))
        when (user.gender) {
            0 -> binding.genderSelector.check(R.id.male)
            1 -> binding.genderSelector.check(R.id.female)
        }
    }

    private fun navigateToMainScreenListener() {
        binding.profileScreenTv.setOnClickListener {
            startMainScreen()
        }
        binding.mainTextInProfileScreen.setOnClickListener {
            startMainScreen()
        }
    }

    private fun navigateToCollectionScreenListener() {
        binding.starTextInMainScreen.setOnClickListener {
            startCollectionScreen()
        }
        binding.profileScreenStar.setOnClickListener {
            startCollectionScreen()
        }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startMainScreen() {
        val intent = Intent(this, MainScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startCollectionScreen() {
        val intent = Intent(this, CollectionScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}