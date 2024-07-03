package com.aloysius.voicehealthguide.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.DrugRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.HistoryRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.NewsRepository
import com.aloysius.voicehealthguide.di.Injection
import com.aloysius.voicehealthguide.view.forgotpassword.ForgotPasswordViewModal
import com.aloysius.voicehealthguide.view.forgotpassword.emailverification.EmailVerificationViewModel
import com.aloysius.voicehealthguide.view.forgotpassword.resetpassword.ResetPasswordViewModel
import com.aloysius.voicehealthguide.view.history.HistoryViewModel
import com.aloysius.voicehealthguide.view.login.LoginViewModel
import com.aloysius.voicehealthguide.view.main.MainViewModel
import com.aloysius.voicehealthguide.view.news.NewsViewModel
import com.aloysius.voicehealthguide.view.profile.ProfileViewModel
import com.aloysius.voicehealthguide.view.register.RegisterViewModel
import kotlinx.coroutines.runBlocking

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val drugRepository: DrugRepository,
    private val historyRepository: HistoryRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, drugRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(historyRepository) as T
            }
            modelClass.isAssignableFrom(ForgotPasswordViewModal::class.java) -> {
                ForgotPasswordViewModal(userRepository) as T
            }
            modelClass.isAssignableFrom(ResetPasswordViewModel::class.java) -> {
                ResetPasswordViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(EmailVerificationViewModel::class.java) -> {
                EmailVerificationViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: runBlocking {
                    ViewModelFactory(
                        Injection.provideRepository(context),
                        Injection.provideDrugRepository(context),
                        Injection.provideHistoryRepository(context)
                    )
                }.also { INSTANCE = it }
            }
        }
    }
}
