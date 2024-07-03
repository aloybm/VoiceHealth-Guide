package com.aloysius.voicehealthguide.view.history

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.data.remote.response.DataItem
import com.aloysius.voicehealthguide.databinding.ListHistoryBinding
import com.aloysius.voicehealthguide.view.detail.HistoryDetailActivity
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(private val context: Context) :
    ListAdapter<DataItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, HistoryDetailActivity::class.java)
            intent.putExtra("history", history)
            holder.itemView.context.startActivity(intent)
        }
    }

    inner class MyViewHolder(private val binding: ListHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: DataItem) {
            val category = history.category
            val formattedText = context.getString(R.string.disease_name_format, category)
            val spannableString = SpannableString(formattedText)

            val startIndex = formattedText.indexOf(category)
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                startIndex + category.length,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.diseaseName.text = spannableString
            binding.textHistory.text = history.keluhan
            binding.dateHistory.text = formatDate(history.createdAt)
        }

        private fun formatDate(isoDate: String): String {
            val inputFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale("id"))
            val date = inputFormat.parse(isoDate)
            return outputFormat.format(date!!)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
