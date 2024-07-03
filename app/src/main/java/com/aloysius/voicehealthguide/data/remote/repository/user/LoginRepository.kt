package com.aloysius.voicehealthguide.data.remote.repository.user


import androidx.lifecycle.liveData
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.response.ErrorResponse
import com.aloysius.voicehealthguide.data.remote.response.LoginResponse
import com.aloysius.voicehealthguide.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class LoginRepository private constructor(
    private val apiService: ApiService,
) {
    fun userLogin(email: String, password: String)= liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.login(email, password)
            emit(ResultState.Success(response))

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(errorResponse.message.let { ResultState.Error(it!!) })
        }
    }

    companion object {
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance(apiService: ApiService): LoginRepository =
            instance ?: synchronized(this) {
                instance ?: LoginRepository(apiService).also { instance = it }
            }
    }
}
