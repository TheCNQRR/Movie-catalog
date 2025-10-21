package com.example.moviecatalog

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviecatalog.databinding.SignUpScreenBinding
import com.example.moviecatalog.logic.AuthLogic

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: SignUpScreenBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        binding = SignUpScreenBinding.inflate(layoutInflater)
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

        binding.haveAccount.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.registerInTheApp.setOnClickListener {
            val login = binding.login.text.toString()
            val email = binding.email.text.toString()
            val name = binding.name.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()
            val birthDate = binding.birthDate.text.toString()
            val gender = when ( binding.genderSelector.checkedRadioButtonId) {
                R.id.male -> 0
                R.id.female -> 1
                else -> null
            }

            val authLogic = AuthLogic()

            authLogic.registerUser(login, email, name, password, confirmPassword, birthDate, gender)
        }
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
