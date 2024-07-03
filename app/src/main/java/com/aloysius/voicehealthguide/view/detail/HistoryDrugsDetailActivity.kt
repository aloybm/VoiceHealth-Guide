package com.aloysius.voicehealthguide.view.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.data.remote.response.HistoryDrugsItem
import com.aloysius.voicehealthguide.databinding.ActivityDrugDetailBinding
import com.aloysius.voicehealthguide.databinding.ActivityHistoryDrugsDetailBinding
import com.bumptech.glide.Glide

class HistoryDrugsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryDrugsDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDrugsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drug = intent.getParcelableExtra<HistoryDrugsItem>("history_drug")
        drug?.let {
            binding.nameDrug.text = it.name
            binding.descDrug.text = it.description
            Glide.with(this)
                .load(it.imageUrl)
                .into(binding.imageDrug)
        }
        setupView()
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