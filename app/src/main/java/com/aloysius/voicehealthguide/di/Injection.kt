package com.aloysius.voicehealthguide.di

import android.content.Context
import com.aloysius.voicehealthguide.data.pref.UserPreference
import com.aloysius.voicehealthguide.data.pref.dataStore
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.DrugRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.ForgotPasswordRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.HistoryRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.LoginRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.NewsRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.RegisterRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.ResetPasswordRepository
import com.aloysius.voicehealthguide.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val registerRepository = RegisterRepository.getInstance(apiService)
        val loginRepository = LoginRepository.getInstance(apiService)
        val newsRepository = NewsRepository.getInstance(apiService)
        val forgotPasswordRepository = ForgotPasswordRepository.getInstance(apiService)
        val resetPasswordRepository = ResetPasswordRepository.getInstance(apiService)
        return UserRepository.getInstance(userPreference, registerRepository, loginRepository,newsRepository, forgotPasswordRepository,resetPasswordRepository )
    }

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }


    fun provideDrugRepository(context: Context): DrugRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getSession().first() }
        TokenManager.setToken(user.token)
        val apiService = ApiConfig.getApiService()
        return DrugRepository.getInstance(apiService)
    }
    fun provideHistoryRepository(context: Context): HistoryRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getSession().first() }
        TokenManager.setToken(user.token)
        val apiService = ApiConfig.getApiService()
        return HistoryRepository.getInstance(apiService)
    }

}

