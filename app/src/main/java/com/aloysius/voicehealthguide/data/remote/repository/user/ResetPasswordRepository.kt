package com.aloysius.voicehealthguide.data.remote.repository.user

import androidx.lifecycle.liveData
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.response.ErrorResponse
import com.aloysius.voicehealthguide.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.http.Query

class ResetPasswordRepository private constructor(
    private val apiService: ApiService,
) {
    fun resetPassword(token: String ,password: String) = liveData {
        try {
            val successResponse = apiService.resetPassword(token, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }

    companion object {
        @Volatile
        private var instance: ResetPasswordRepository? = null

        fun getInstance(apiService: ApiService): ResetPasswordRepository =
            instance ?: synchronized(this) {
                instance ?: ResetPasswordRepository(apiService).also { instance = it }
            }
    }
}