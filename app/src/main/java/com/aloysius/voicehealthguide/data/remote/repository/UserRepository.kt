package com.aloysius.voicehealthguide.data.remote.repository


import com.aloysius.voicehealthguide.data.pref.UserModel
import com.aloysius.voicehealthguide.data.pref.UserPreference
import com.aloysius.voicehealthguide.data.remote.repository.user.ForgotPasswordRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.LoginRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.NewsRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.RegisterRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.ResetPasswordRepository
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val registerRepository: RegisterRepository,
    private val loginRepository: LoginRepository,
    private val newsRepository: NewsRepository,
    private val forgotPasswordRepository: ForgotPasswordRepository,
    private val resetPasswordRepository: ResetPasswordRepository
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getRegisterRepository(): RegisterRepository {
        return registerRepository
    }

    fun getLoginRepository(): LoginRepository {
        return loginRepository
    }
    fun getNewsRepository(): NewsRepository {
        return newsRepository
    }

    fun getForgotPasswordRepository(): ForgotPasswordRepository {
        return forgotPasswordRepository
    }
    fun getResetPasswordRepository(): ResetPasswordRepository {
        return resetPasswordRepository
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            registerRepository: RegisterRepository,
            loginRepository: LoginRepository,
            newsRepository: NewsRepository,
            forgotPasswordRepository: ForgotPasswordRepository,
            resetPasswordRepository: ResetPasswordRepository
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, registerRepository, loginRepository, newsRepository, forgotPasswordRepository,resetPasswordRepository).also { instance = it }
            }
    }
}
