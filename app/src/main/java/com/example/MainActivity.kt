package com.example

import android.os.Bundle
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.databinding.ActivityMainBinding
import com.example.presentation.CatalogFragment
import com.example.presentation.FavoritesFragment
import com.example.presentation.AccountFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the active indicator color programmatically to match design
        binding.bottomNavigation.itemActiveIndicatorColor = ColorStateList.valueOf(
            ContextCompat.getColor(this, R.color.bottom_nav_indicator)
        )

        // Ensure proper bottom padding to avoid system navigation overlapping
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigation) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val density = resources.displayMetrics.density
            val defaultPaddingTop = (12 * density).toInt()
            val defaultPaddingBottom = (12 * density).toInt()
            view.setPadding(
                view.paddingLeft,
                defaultPaddingTop,
                view.paddingRight,
                defaultPaddingBottom + systemBars.bottom
            )
            insets
        }

        setupNavigation()

        if (savedInstanceState == null) {
            loadFragment(CatalogFragment())
        }
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.action_catalog -> CatalogFragment()
                R.id.action_favorites -> FavoritesFragment()
                R.id.action_account -> AccountFragment()
                else -> CatalogFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .commit()
    }
}
