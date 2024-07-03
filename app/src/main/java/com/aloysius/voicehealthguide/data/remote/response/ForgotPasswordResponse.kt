package com.aloysius.voicehealthguide.data.remote.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(

	@field:SerializedName("data")
	val data: ForgotPasswordData,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class ForgotPasswordData(

	@field:SerializedName("email")
	val email: String
)
