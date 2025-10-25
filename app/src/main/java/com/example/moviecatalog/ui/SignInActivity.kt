package com.example.moviecatalog.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.RetrofitClient
import com.example.moviecatalog.databinding.SignInScreenBinding
import com.example.moviecatalog.logic.AuthLogic

class SignInActivity: AppCompatActivity() {
    private lateinit var binding: SignInScreenBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        binding = SignInScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemBars()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.root.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_DOWN) {
                hideKeyboardAndClearFocus()
            }
            false
        }

        binding.registerInTheApp.setOnClickListener {
            onButtonClick(binding.registerInTheApp)
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupTextWatcher()

        binding.loginToTheApp.setOnClickListener {
            val login = binding.signInLoginInput.text.toString()
            val password = binding.signInLoginPassword.text.toString()

            onButtonClick(binding.loginToTheApp)

            val authLogic = AuthLogic(
                authApi = RetrofitClient.getAuthApi(),
                context = this,
                onError = { message ->
                    binding.errorMessage.text = message
                    binding.errorMessage.visibility = View.VISIBLE
                },
                onClearErrors = { binding.errorMessage.visibility = View.GONE },
                onSuccess = { println(getString(R.string.success_login)) }
            )

            authLogic.login(login, password)
        }
    }

    private fun isAllFieldsFilled(): Boolean {
        return binding.signInLoginInput.text.toString().isNotEmpty() &&
                binding.signInLoginPassword.text.toString().isNotEmpty()
    }

    private fun checkFieldsAndUpdateButton() {
        val allFieldsFilled = isAllFieldsFilled()

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

    private fun onButtonClick(view: View) {
        view.animate()
            .scaleX(0.8f)
            .scaleY(0.8f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun hideKeyboardAndClearFocus() {
        val currentFocus = currentFocus
        if (currentFocus is EditText) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)

            currentFocus.clearFocus()
            currentFocus.isFocusable = false
            currentFocus.isFocusableInTouchMode = false

            currentFocus.post {
                currentFocus.isFocusable = true
                currentFocus.isFocusableInTouchMode = true
            }
        }
    }
}