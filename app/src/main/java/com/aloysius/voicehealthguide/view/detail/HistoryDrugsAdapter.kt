package com.aloysius.voicehealthguide.view.detail

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.data.remote.response.HistoryDrugsItem
import com.aloysius.voicehealthguide.databinding.ListItemHistoryDrugBinding
import com.bumptech.glide.Glide

class HistoryDrugsAdapter : ListAdapter<HistoryDrugsItem, HistoryDrugsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemHistoryDrugBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val drug = getItem(position)
        holder.bind(drug)
        holder.binding.detailButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, HistoryDrugsDetailActivity::class.java)
            intent.putExtra("history_drug", drug)
            holder.itemView.context.startActivity(intent)
        }
    }

    inner class MyViewHolder(val binding: ListItemHistoryDrugBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(drug: HistoryDrugsItem) {
            binding.nameHistoryDrug.text = drug.name
            if (drug.imageUrl.isNotEmpty()) {
                Glide.with(binding.root)
                    .load(drug.imageUrl)
                    .into(binding.imageDrug)
            } else {
                Glide.with(binding.root)
                    .load(R.drawable.ic_place_holder)
                    .into(binding.imageDrug)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryDrugsItem>() {
            override fun areItemsTheSame(oldItem: HistoryDrugsItem, newItem: HistoryDrugsItem): Boolean {
                return oldItem.id == newItem.id // Adjust this comparison based on your data model
            }

            override fun areContentsTheSame(oldItem: HistoryDrugsItem, newItem: HistoryDrugsItem): Boolean {
                return oldItem == newItem // Adjust this comparison based on your data model
            }
        }
    }
}