package com.aloysius.voicehealthguide.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.response.DrugsData
import com.aloysius.voicehealthguide.databinding.ActivityMainBinding
import com.aloysius.voicehealthguide.view.ViewModelFactory
import com.aloysius.voicehealthguide.view.history.HistoryActivity
import com.aloysius.voicehealthguide.view.news.NewsActivity
import com.aloysius.voicehealthguide.view.profile.ProfileActivity
import com.aloysius.voicehealthguide.view.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage.ENGLISH
import com.google.mlkit.nl.translate.TranslateLanguage.INDONESIAN
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var micIV: ImageView
    private lateinit var speechRecognizerLauncher: ActivityResultLauncher<Intent>
    private val recordAudioRequestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BottomSheetBehavior.from(binding.sheet).apply {
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.selectedItemId = R.id.item_2
        micIV = binding.micImg

        val layoutManager = GridLayoutManager(this, 2)
        binding.rvDrug.layoutManager = layoutManager

        val adapter = DrugAdapter()
        binding.rvDrug.adapter = adapter

        speechRecognizerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val res: ArrayList<String>? =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (!res.isNullOrEmpty()) {
                    showLoading(true)
                    Log.d("Text", res[0])
                    translateText(res[0])
                }
            }
        }

        micIV.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    recordAudioRequestCode
                )
            } else {
                startSpeechRecognition()
            }
        }

        lifecycleScope.launch {
            viewModel.getSession().observe(this@MainActivity) { user ->
                if (!user.isLogin) {
                    navigateToWelcome()
                } else {
                    binding.nameText.text = user.name
                }
            }
        }

        setGreetingMessage()
        setupView()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_1 -> {
                    val intent = Intent(this, NewsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }

                R.id.item_3 -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        showButtonAction()
        profileButton()
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

    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "id")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id")
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.katakan_keluhanmu))
        }
        try {
            speechRecognizerLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, " " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun profileButton() {
        binding.profileImage.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun translateText(detectedText: String?) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(INDONESIAN)
            .setTargetLanguage(ENGLISH)
            .build()
        val indonesianEnglishTranslator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        indonesianEnglishTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                indonesianEnglishTranslator.translate(detectedText.toString())
                    .addOnSuccessListener { translatedText ->
                        Log.d("translate", translatedText)
                        postDrugs(translatedText)
                        indonesianEnglishTranslator.close()

                    }
                    .addOnFailureListener { exception ->
                        showToast(exception.message.toString())
                        print(exception.stackTrace)
                        indonesianEnglishTranslator.close()
                    }
            }
            .addOnFailureListener { exception ->
                showToast(getString(R.string.downloading_model_fail))
            }

        lifecycle.addObserver(indonesianEnglishTranslator)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == recordAudioRequestCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startSpeechRecognition()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun postDrugs(keluhan: String) {
        showLoading(true)
        Log.d("Keluhan", keluhan)
        viewModel.getDrugs(keluhan).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        Log.d("Keluhan", result.data.data.drugs.toString())
                        val drugsDataList = result.data.data.drugs.map { drugsItem ->
                            DrugsData(
                                drugs = listOf(drugsItem),
                                category = result.data.data.category
                            )
                        }
                        val adapter = DrugAdapter()
                        adapter.submitList(drugsDataList)
                        binding.rvDrug.adapter = adapter
                        val category = result.data.data.category
                        val formattedText = getString(R.string.disease_name_format, category)
                        val spannableString = SpannableString(formattedText)
                        binding.diseaseName.visibility = View.VISIBLE
                        val startIndex = formattedText.indexOf(category)
                        spannableString.setSpan(
                            StyleSpan(Typeface.BOLD),
                            startIndex,
                            startIndex + category.length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        binding.diseaseName.text = spannableString

                        BottomSheetBehavior.from(binding.sheet).apply {
                            this.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                        showLoading(false)

                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        Log.e("error", result.error)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showButtonAction() {
        binding.actionButton.setOnClickListener {
            BottomSheetBehavior.from(binding.sheet).apply {
                this.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
    private fun setGreetingMessage() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 0..11 -> getString(R.string.good_morning)
            in 12..17 -> getString(R.string.good_afternoon)
            else -> getString(R.string.good_evening)
        }
        binding.greetingText.text = greeting
    }

}
