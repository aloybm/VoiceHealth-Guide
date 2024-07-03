package com.aloysius.voicehealthguide.data.remote.repository.user

import androidx.lifecycle.liveData
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.response.ErrorResponse
import com.aloysius.voicehealthguide.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class DrugRepository private constructor(
    private val apiService: ApiService,
) {
    fun getDrugs(keluhan: String) = liveData {
        try {
            val successResponse = apiService.getDrug(keluhan)
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
        private var instance: DrugRepository? = null

        fun getInstance(apiService: ApiService): DrugRepository =
            instance ?: synchronized(this) {
                instance ?: DrugRepository(apiService).also { instance = it }
            }
    }
}