package com.aloysius.voicehealthguide.view.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.data.remote.response.DataItem
import com.aloysius.voicehealthguide.data.remote.response.DrugsItem
import com.aloysius.voicehealthguide.data.remote.response.HistoryDrugsItem
import com.aloysius.voicehealthguide.databinding.ActivityDrugDetailBinding
import com.aloysius.voicehealthguide.databinding.ActivityHistoryDetailBinding
import com.bumptech.glide.Glide

class HistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryDetailBinding
    private lateinit var adapter: HistoryDrugsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HistoryDrugsAdapter()

        binding.rvHistory.layoutManager = GridLayoutManager(this, 2)

        binding.rvHistory.adapter = adapter

        val history = intent.getParcelableExtra<DataItem>("history")

        history?.let {
            adapter.submitList(it.drugs)
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