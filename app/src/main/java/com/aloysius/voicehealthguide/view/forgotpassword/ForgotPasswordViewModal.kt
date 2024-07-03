package com.aloysius.voicehealthguide.view.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.response.ForgotPasswordResponse
import com.aloysius.voicehealthguide.data.remote.response.RegisterResponse

class ForgotPasswordViewModal(private val repository: UserRepository) : ViewModel() {
    fun forgotPassword(email: String): LiveData<ResultState<ForgotPasswordResponse>> {
        return repository.getForgotPasswordRepository().userForgotPassword(email)
    }
}