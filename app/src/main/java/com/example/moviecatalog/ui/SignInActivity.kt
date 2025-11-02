package com.example.moviecatalog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.moviecatalog.MovieCatalogApplication
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.RetrofitClient
import com.example.moviecatalog.databinding.SignInScreenBinding
import com.example.moviecatalog.logic.AuthLogic
import com.example.moviecatalog.logic.util.TokenManager
import com.example.moviecatalog.logic.util.Validator
import kotlinx.coroutines.launch

class SignInActivity: AppCompatActivity() {
    private lateinit var binding: SignInScreenBinding
    private val effects = Effects()
    private val validator = Validator(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        binding = SignInScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        effects.hideSystemBars(window)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.root.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_DOWN) {
                effects.hideKeyboardAndClearFocus(this, currentFocus)
            }
            false
        }

        binding.registerInTheApp.setOnClickListener {
            effects.onButtonClick(binding.registerInTheApp)
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupTextWatcher()

        binding.loginToTheApp.setOnClickListener {
            val login = binding.signInLoginInput.text.toString()
            val password = binding.signInLoginPassword.text.toString()

            effects.onButtonClick(binding.loginToTheApp)

            val authLogic = AuthLogic(
                authApi = RetrofitClient.getAuthApi(),
                tokenManager = getTokenManager(),
                context = this,
                onError = { message ->
                    binding.errorMessage.text = message
                    binding.errorMessage.visibility = View.VISIBLE
                },
                onClearErrors = { binding.errorMessage.visibility = View.GONE },
                onSuccess = {
                    val intent = Intent(this, MainScreenActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            )

            lifecycleScope.launch {
                authLogic.login(login, password)
            }
        }
    }

    private fun checkFieldsAndUpdateButton() {
        val login = binding.signInLoginInput.text.toString()
        val password = binding.signInLoginPassword.text.toString()

        val allFieldsFilled = validator.isAllFieldsFilledLogin(login, password)

        if (allFieldsFilled) {
            binding.loginToTheApp.setTextColor(getColor(R.color.white))
            binding.loginToTheApp.setBackgroundResource(R.drawable.active_button)
        } else {
            binding.loginToTheApp.setBackgroundResource(R.drawable.authorization_button_border)
            binding.loginToTheApp.setTextColor(getColor(R.color.accent))
        }
    }

    private fun setupTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checkFieldsAndUpdateButton()
            }
        }

        binding.signInLoginInput.addTextChangedListener(textWatcher)
        binding.signInLoginPassword.addTextChangedListener(textWatcher)
    }

    private fun getTokenManager(): TokenManager {
        return (application as MovieCatalogApplication).tokenManager
    }
}