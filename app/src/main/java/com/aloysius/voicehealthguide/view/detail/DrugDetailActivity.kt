package com.aloysius.voicehealthguide.view.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.aloysius.voicehealthguide.data.remote.response.DrugsItem
import com.aloysius.voicehealthguide.data.remote.response.HistoryDrugsItem
import com.aloysius.voicehealthguide.databinding.ActivityDrugDetailBinding
import com.bumptech.glide.Glide

class DrugDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrugDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrugDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drug = intent.getParcelableExtra<DrugsItem>("drugItem")
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