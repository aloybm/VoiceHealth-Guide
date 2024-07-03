package com.aloysius.voicehealthguide.view.login

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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.pref.UserModel
import com.aloysius.voicehealthguide.databinding.ActivityLoginBinding
import com.aloysius.voicehealthguide.view.ViewModelFactory
import com.aloysius.voicehealthguide.view.customview.EmailEditText
import com.aloysius.voicehealthguide.view.customview.PasswordEditText
import com.aloysius.voicehealthguide.view.forgotpassword.ForgotPasswordActivity
import com.aloysius.voicehealthguide.view.forgotpassword.resetpassword.ResetPasswordActivity
import com.aloysius.voicehealthguide.view.main.MainActivity
import com.aloysius.voicehealthguide.view.register.RegisterActivity


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = binding.edLoginEmail
        passwordEditText = binding.edLoginPassword


        setupView()
        setupAction()
        navigateToRegister()
        playAnimation()
        navigateToForgotPassword()
        handleIncomingIntent(intent)

    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent(intent)
    }


    private fun handleIncomingIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val uri: Uri? = intent.data
            uri?.getQueryParameter("token")?.let { token ->
                when (uri.path) {
                    "/api/reset-password" -> {
                        val resetPasswordIntent = Intent(this, ResetPasswordActivity::class.java).apply {
                            putExtra("token", token)
                        }
                        startActivity(resetPasswordIntent)
                        finish()
                    }
                }
            }
        }
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
        binding.loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (validateInputText()) {
                showLoading(true)
                viewModel.login(email, password)
                viewModel.login(email, password).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showLoading(false)
                            val name = result.data.data.user.name
                            val username = result.data.data.user.username
                            val token = result.data.data.accessToken
                            saveSessionAndProceed(UserModel(name, username, email, token, true, 0L))
                        }

                        is ResultState.Error -> {
                            showLoading(false)
                            showErrorDialog(result.error)
                        }
                    }
                }
            } else {
                binding.loginButton.isEnabled = false
            }
        }

        binding.edLoginEmail.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            validateInputText()
        })
        binding.edLoginPassword.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            validateInputText()
        })

        validateInputText()
    }


    private fun validateInputText(): Boolean {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 8

        binding.loginButton.isEnabled = isEmailValid && isPasswordValid
        return isEmailValid && isPasswordValid
    }

    private fun saveSessionAndProceed(userModel: UserModel) {
        viewModel.saveSession(userModel)
        showLoginSuccessDialog()
    }
private fun navigateToForgotPassword() {
    binding.forgotPassword.setOnClickListener {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }
}
    private fun showLoginSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(context.getString(R.string.login_succes_dialog))
            setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun navigateToRegister() {
        binding.signUpNav.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
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
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailEditTextLayout,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}
