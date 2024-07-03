package com.aloysius.voicehealthguide.view.forgotpassword.resetpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.ResetPasswordRepository
import com.aloysius.voicehealthguide.data.remote.response.ResetPasswordResponse
import kotlinx.coroutines.launch
import retrofit2.http.Query

class ResetPasswordViewModel(private val repository: UserRepository) : ViewModel() {
    fun resetPassword(query: String, password: String): LiveData<ResultState<ResetPasswordResponse>> {
        return repository.getResetPasswordRepository().resetPassword(query, password)
    }

}
