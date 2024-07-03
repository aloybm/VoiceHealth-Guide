package com.aloysius.voicehealthguide.view.forgotpassword

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
import androidx.core.widget.addTextChangedListener
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.databinding.ActivityForgotPasswordBinding
import com.aloysius.voicehealthguide.view.ViewModelFactory
import com.aloysius.voicehealthguide.view.customview.EmailEditText
import com.aloysius.voicehealthguide.view.forgotpassword.emailverification.EmailVerificationActivity

class ForgotPasswordActivity : AppCompatActivity() {
    private val viewModel by viewModels<ForgotPasswordViewModal> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var emailEditText: EmailEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = binding.edLoginEmail

        setupView()
        setupAction()
        playAnimation()
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

    private fun setupAction() {
        binding.sendButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (validateInputText()) {
                showLoading(true)
                viewModel.forgotPassword(email).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showLoading(false)
                            navigateToEmailVerificationActivity(email)
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

        binding.edLoginEmail.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            validateInputText()
        })

        validateInputText()
    }

    private fun validateInputText(): Boolean {
        val email = emailEditText.text.toString()

        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        binding.sendButton.isEnabled = isEmailValid
        return isEmailValid
    }


    private fun navigateToEmailVerificationActivity(email: String) {
        val intent = Intent(this, EmailVerificationActivity::class.java)
        intent.putExtra(EmailVerificationActivity.EXTRA_EMAIL, email)
        startActivity(intent)
        finish()
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
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)

        val login = ObjectAnimator.ofFloat(binding.sendButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailEditTextLayout,
                login,
                desc
            )
            startDelay = 100
        }.start()
    }
}
