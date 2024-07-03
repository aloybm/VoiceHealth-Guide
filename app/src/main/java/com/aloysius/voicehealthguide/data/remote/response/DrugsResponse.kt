package com.aloysius.voicehealthguide.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DrugsResponse(

	@field:SerializedName("data")
	val data: DrugsData,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)
@Parcelize
data class DrugsData(

	@field:SerializedName("drugs")
	val drugs: List<DrugsItem>,

	@field:SerializedName("category")
	val category: String
): Parcelable
@Parcelize
data class DrugsItem(

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String
): Parcelable
