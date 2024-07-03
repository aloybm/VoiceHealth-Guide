package com.aloysius.voicehealthguide.view.forgotpassword.resetpassword

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.pref.UserModel
import com.aloysius.voicehealthguide.databinding.ActivityLoginBinding
import com.aloysius.voicehealthguide.databinding.ActivityResetPasswordBinding
import com.aloysius.voicehealthguide.di.Injection
import com.aloysius.voicehealthguide.view.ViewModelFactory
import com.aloysius.voicehealthguide.view.customview.EmailEditText
import com.aloysius.voicehealthguide.view.customview.PasswordEditText
import com.aloysius.voicehealthguide.view.forgotpassword.ForgotPasswordActivity
import com.aloysius.voicehealthguide.view.login.LoginActivity
import com.aloysius.voicehealthguide.view.login.LoginViewModel
import com.aloysius.voicehealthguide.view.main.MainActivity
import com.aloysius.voicehealthguide.view.register.RegisterActivity
import kotlinx.coroutines.launch

class ResetPasswordActivity : AppCompatActivity() {
    private val viewModel by viewModels<ResetPasswordViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var passwordEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        passwordEditText = binding.edResetPassword


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
        binding.resetButton.setOnClickListener {
            val password = passwordEditText.text.toString()
            val token = intent.getStringExtra("token")
            if (validateInputText()) {
                showLoading(true)
                viewModel.resetPassword(token!!,password).observe(this) { result ->
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
                binding.resetButton.isEnabled = false
            }
        }

        binding.edResetPassword.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            validateInputText()
        })

        validateInputText()
    }


    private fun validateInputText(): Boolean {
        val password = passwordEditText.text.toString()

        val isPasswordValid = password.length >= 8

        binding.resetButton.isEnabled = isPasswordValid
        return isPasswordValid
    }

    private fun showLoginSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(context.getString(R.string.reset_password_dialog))
            setPositiveButton("Login") { _, _ ->
                val intent = Intent(context, LoginActivity::class.java)
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
        ObjectAnimator.ofFloat(binding.imgResetPwd, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)

        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.resetButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}