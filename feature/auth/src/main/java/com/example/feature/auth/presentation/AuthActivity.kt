package com.example.feature.auth.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.feature.auth.R
import com.example.feature.auth.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment())
                .commit()
        }
    }
}
