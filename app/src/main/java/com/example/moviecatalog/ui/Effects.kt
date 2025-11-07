package com.example.moviecatalog.ui

import android.content.Context
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class Effects {
    companion object {
        private const val SMALL_SCALE = 0.8f
        private const val NORMAL_SCALE = 1f
        private const val DURATION = 100L
    }
    fun onButtonClick(view: View) {
        view.animate()
            .scaleX(SMALL_SCALE)
            .scaleY(SMALL_SCALE)
            .setDuration(DURATION)
            .withEndAction {
                view.animate()
                    .scaleX(NORMAL_SCALE)
                    .scaleY(NORMAL_SCALE)
                    .setDuration(DURATION)
                    .start()
            }
            .start()
    }

    fun hideSystemBars(window: Window) {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun hideKeyboardAndClearFocus(context: Context, currentFocus: View?) {
        if (currentFocus is EditText) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
