package com.aloysius.voicehealthguide.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.aloysius.voicehealthguide.view.customview.EmailEditText
import com.aloysius.voicehealthguide.view.customview.PasswordEditText
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.databinding.ActivityRegisterBinding
import com.aloysius.voicehealthguide.di.DialogType
import com.aloysius.voicehealthguide.view.ViewModelFactory
import com.aloysius.voicehealthguide.view.customview.NameEditText
import com.aloysius.voicehealthguide.view.customview.UsernameEditText
import com.aloysius.voicehealthguide.view.login.LoginActivity
import com.aloysius.voicehealthguide.view.news.NewsActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var nameEditText: NameEditText
    private lateinit var usernameEditText : UsernameEditText
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = binding.edRegisterEmail
        passwordEditText = binding.edRegisterPassword
        nameEditText = binding.edRegisterName
        usernameEditText = binding.edRegisterUsername

        setupView()
        setupAction()
        navigateToLogin()
        playAnimation()
        binding.signupButton.isEnabled = false
    }
    private fun navigateToLogin(){
        binding.signIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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
        binding.signupButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val username = binding.edRegisterUsername.text.toString()

            if (validateInputText()) {
                registerUser(username, name, email, password)
            } else {
                binding.signupButton.isEnabled = false
            }
        }

        binding.edRegisterEmail.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            validateInputText()
        })
        binding.edRegisterPassword.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            validateInputText()
        })
        binding.edRegisterName.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            validateInputText()
        })
        binding.edRegisterUsername.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            validateInputText()
        })
    }

    private fun validateInputText(): Boolean {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val name = nameEditText.text.toString()
        val username = usernameEditText.text.toString()

        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 8
        val isNameValid = name.isNotEmpty()
        val isUsernameValid = username.isNotEmpty() && !username.contains(" ")

        binding.signupButton.isEnabled = isEmailValid && isPasswordValid && isNameValid && isUsernameValid
        return isEmailValid && isPasswordValid && isNameValid && isUsernameValid
    }

    private fun registerUser(username: String, name: String, email: String, password: String) {
        registerViewModel.registerUser(username, name, email, password).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    showAlertDialog(
                        title = "Success",
                        Message = "Cek Email Anda!",
                        type = DialogType.SUCCESS,
                    ) {
                        finish()
                    }
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showAlertDialog(
                        title = "Fail",
                        Message = "Login gagal. Pastikan email dan password Anda benar.",
                        type = DialogType.ERROR,
                    ) {}
                }

                null -> {
                }
            }
        }
    }

    private fun showAlertDialog(
        title: String,
        Message: String,
        type: DialogType,
        doAction: () -> Unit
    ) {
        val builder = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(Message)
            setPositiveButton("Lanjut") { _, _ -> doAction() }
        }

        val alertDialog: AlertDialog = builder.create().apply {
            setCancelable(false)
            show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            when (type) {
                DialogType.ERROR -> {}
                DialogType.SUCCESS -> doAction()
            }
            alertDialog.dismiss()
        }, DELAY_TIME)
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
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailEditTextLayout,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }

    companion object {
        private const val DELAY_TIME = 2000L
    }
}