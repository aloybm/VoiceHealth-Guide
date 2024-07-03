package com.aloysius.voicehealthguide.view.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.data.remote.response.DrugsData
import com.aloysius.voicehealthguide.data.remote.response.DrugsItem
import com.aloysius.voicehealthguide.databinding.ListDrugBinding
import com.aloysius.voicehealthguide.view.detail.DrugDetailActivity
import com.bumptech.glide.Glide

class DrugAdapter : ListAdapter<DrugsData, DrugAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListDrugBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val drugsData = getItem(position)
        holder.bind(drugsData)
    }

    inner class MyViewHolder(private val binding: ListDrugBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(drugsData: DrugsData) {
            val resultDrugItem = drugsData.drugs.firstOrNull()

            if (resultDrugItem != null) {
                binding.productName.text = resultDrugItem.name
                if (resultDrugItem.imageUrl.isNotEmpty()) {
                    Glide.with(binding.root)
                        .load(resultDrugItem.imageUrl)
                        .into(binding.productImage)
                } else {
                    Glide.with(binding.root)
                        .load(R.drawable.ic_place_holder)
                        .into(binding.productImage)
                }

                binding.detailButton.setOnClickListener {
                    val intent = Intent(binding.root.context, DrugDetailActivity::class.java)
                    intent.putExtra("drugItem", resultDrugItem)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DrugsData>() {
            override fun areItemsTheSame(oldItem: DrugsData, newItem: DrugsData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DrugsData, newItem: DrugsData): Boolean {
                return oldItem == newItem
            }
        }
    }
}
