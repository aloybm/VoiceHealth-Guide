package com.aloysius.voicehealthguide.view.forgotpassword.emailverification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.response.ForgotPasswordResponse

class EmailVerificationViewModel(private val repository: UserRepository) : ViewModel() {
    fun sendBack(email: String): LiveData<ResultState<ForgotPasswordResponse>> {
        return repository.getForgotPasswordRepository().userForgotPassword(email)
    }
}