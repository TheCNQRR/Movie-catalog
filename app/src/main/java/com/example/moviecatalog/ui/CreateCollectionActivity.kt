package com.example.moviecatalog.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviecatalog.databinding.CreateCollectionScreenBinding

class CreateCollectionActivity: AppCompatActivity() {
    private lateinit var binding: CreateCollectionScreenBinding
    private val effects = Effects()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setupWindow()
        setupBinding()
        setupWindowInsest()

        binding.backIcon.setOnClickListener {
            returnToCollectionScreen()
        }
    }

    private fun setupWindow() {
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        effects.hideSystemBars(window)
    }

    private fun setupBinding() {
        binding = CreateCollectionScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupWindowInsest() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun returnToCollectionScreen() {
        finish()
    }
}