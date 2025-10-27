package com.example.moviecatalog.ui

import android.content.Context
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.WindowCompat

class Effects {
    fun onButtonClick(view: View) {
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

    fun hideSystemBars(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
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