package com.example.moviecatalog.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
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
import com.example.moviecatalog.databinding.SignUpScreenBinding
import com.example.moviecatalog.logic.AuthLogic
import com.google.android.material.datepicker.MaterialDatePicker

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: SignUpScreenBinding
    private var selectedBirthDate: String = ""

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
            onButtonClick(binding.haveAccount)
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.birthDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.select_date))
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTheme(R.style.ThemeOverlay_MovieCatalog_DatePicker)
                    .build()
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")


            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = selectedDate
                }
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)

                val date = "$day.$month.$year"
                binding.birthDate.text = date

                val apiDate = "${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}T00:00:00.000Z"

                selectedBirthDate = apiDate
            }
        }

        setupTextWatcher()

        binding.registerInTheApp.setOnClickListener {
            val login = binding.login.text.toString()
            val email = binding.email.text.toString()
            val name = binding.name.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()
            val birthDate = selectedBirthDate
            val gender = when ( binding.genderSelector.checkedRadioButtonId) {
                R.id.male -> 0
                R.id.female -> 1
                else -> null
            }

            onButtonClick(binding.registerInTheApp)

            val authLogic = AuthLogic(
                authApi = RetrofitClient.getAuthApi(),
                context = this,
                onError = { message ->
                    binding.errorMessage.text = message
                    binding.errorMessage.visibility = View.VISIBLE
                },
                onClearErrors = { binding.errorMessage.visibility = View.GONE },
                onSuccess = {
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            )

            authLogic.registerUser(login, email, name, password, confirmPassword, birthDate, gender)
        }
    }

    private fun isAllFieldsFilled(): Boolean {
        return binding.login.text.toString().isNotEmpty() &&
                binding.email.text.toString().isNotEmpty() &&
                binding.name.text.toString().isNotEmpty() &&
                binding.password.text.toString().isNotEmpty() &&
                binding.confirmPassword.text.toString().isNotEmpty() &&
                binding.birthDate.text.toString().isNotEmpty() &&
                binding.genderSelector.checkedRadioButtonId != -1
    }

    private fun checkFieldsAndUpdateButton() {
        val allFieldsFilled = isAllFieldsFilled()

        if (allFieldsFilled) {
            binding.registerInTheApp.setBackgroundResource(R.drawable.active_button)
            binding.registerInTheApp.setTextColor(getColor(R.color.white))
        } else {
            binding.registerInTheApp.setBackgroundResource(R.drawable.authorization_button_border)
            binding.registerInTheApp.setTextColor(getColor(R.color.accent))
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

        binding.login.addTextChangedListener(textWatcher)
        binding.email.addTextChangedListener(textWatcher)
        binding.name.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
        binding.confirmPassword.addTextChangedListener(textWatcher)
        binding.birthDate.addTextChangedListener(textWatcher)
        binding.genderSelector.setOnCheckedChangeListener { _, _ ->
            checkFieldsAndUpdateButton()
        }
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
