package com.example.feature.onboarding.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.feature.onboarding.databinding.ActivityOnboardingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private val viewModel: OnboardingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnContinue.setOnClickListener {
            navigateToAuth()
        }

        // Disable scrolling on row containers to keep the tag cloud static and interactive-free
        val touchListener = android.view.View.OnTouchListener { _, _ -> true }
        binding.hsvRow1.setOnTouchListener(touchListener)
        binding.hsvRow2.setOnTouchListener(touchListener)
        binding.hsvRow3.setOnTouchListener(touchListener)
        binding.hsvRow4.setOnTouchListener(touchListener)
        binding.hsvRow5.setOnTouchListener(touchListener)
    }

    private fun navigateToAuth() {
        try {
            val intent = Intent(this, Class.forName("com.example.feature.auth.presentation.AuthActivity"))
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
