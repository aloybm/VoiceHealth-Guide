package com.aloysius.voicehealthguide.view.forgotpassword.emailverification

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.databinding.ActivityEmailVerificationBinding
import com.aloysius.voicehealthguide.view.ViewModelFactory
import com.aloysius.voicehealthguide.view.main.MainActivity
import com.aloysius.voicehealthguide.view.welcome.WelcomeActivity

class EmailVerificationActivity : AppCompatActivity() {
    private val viewModel by viewModels<EmailVerificationViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityEmailVerificationBinding
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra(EXTRA_EMAIL) ?: ""

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.sendButton.setOnClickListener {
            val emailValue = email
            if (validateInputText()) {
                showLoading(true)
                viewModel.sendBack(emailValue).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            showLoading(false)
                            showLoginSuccessDialog()
                        }
                        is ResultState.Error -> {
                            showLoading(false)
                            showErrorDialog(result.error)
                        }
                    }
                }
            } else {
                binding.sendButton.isEnabled = false
            }
        }
    }

    private fun validateInputText(): Boolean {
        val emailValue = email
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()
        binding.sendButton.isEnabled = isEmailValid
        return isEmailValid
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

    private fun showLoginSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(context.getString(R.string.email_verification_dialog))
            setPositiveButton("Kembali") { _, _ ->
                val intent = Intent(context, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }


    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("OK", null)
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgForgotPassword, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.textForgotPassword, View.ALPHA, 1f).setDuration(100)


        val login = ObjectAnimator.ofFloat(binding.sendButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                login,
                desc
            )
            startDelay = 100
        }.start()
    }
    companion object {
        const val EXTRA_EMAIL = "extra_email"
    }
}