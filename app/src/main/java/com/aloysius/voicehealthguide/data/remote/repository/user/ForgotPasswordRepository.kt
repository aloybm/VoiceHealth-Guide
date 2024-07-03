package com.aloysius.voicehealthguide.data.remote.repository.user

import androidx.lifecycle.liveData
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.response.ErrorResponse
import com.aloysius.voicehealthguide.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class ForgotPasswordRepository private constructor(
    private val apiService: ApiService,
) {
    fun userForgotPassword(email: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.forgotPassword(email)
            emit(ResultState.Success(response))

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(errorResponse.message.let { ResultState.Error(it!!) })
        }
    }

    companion object {
        @Volatile
        private var instance: ForgotPasswordRepository? = null

        fun getInstance(apiService: ApiService): ForgotPasswordRepository =
            instance ?: synchronized(this) {
                instance ?: ForgotPasswordRepository(apiService).also { instance = it }
            }
    }
}