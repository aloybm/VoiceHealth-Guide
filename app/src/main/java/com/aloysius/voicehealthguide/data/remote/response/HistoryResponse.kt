package com.aloysius.voicehealthguide.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)
@Parcelize
data class HistoryDrugsItem(

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String
) :Parcelable
@Parcelize
data class DataItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("drugs")
	val drugs: List<HistoryDrugsItem>,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("keluhan")
	val keluhan: String
) : Parcelable
