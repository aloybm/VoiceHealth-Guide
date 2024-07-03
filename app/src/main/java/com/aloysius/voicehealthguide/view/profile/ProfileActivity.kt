package com.aloysius.voicehealthguide.view.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.databinding.ActivityNewsBinding
import com.aloysius.voicehealthguide.databinding.ActivityProfileBinding
import com.aloysius.voicehealthguide.view.ViewModelFactory
import com.aloysius.voicehealthguide.view.news.NewsViewModel
import com.aloysius.voicehealthguide.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.getSession().observe(this@ProfileActivity) { user ->
                binding.nameProfile.text = user.name
                binding.usernameProfile.text = user.username
            }
        }
        binding.logoutButton.setOnClickListener{
            lifecycleScope.launch {
                try {
                    viewModel.logout()
                    navigateToWelcome()
                    Log.d("Profile Activity", "User logged out, navigating to WelcomeActivity")
                } catch (e: Exception) {
                    showToast("Failed to logout: ${e.message}")
                }
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun navigateToWelcome() {
        val intent = Intent(this, WelcomeActivity::class.java)
        Log.d("MainActivity", "Navigating to WelcomeActivity")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
