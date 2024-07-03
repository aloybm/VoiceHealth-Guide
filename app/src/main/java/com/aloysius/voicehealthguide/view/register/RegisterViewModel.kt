package com.aloysius.voicehealthguide.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.response.RegisterResponse

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun registerUser(username: String, name: String, email: String, password: String): LiveData<ResultState<RegisterResponse>> {
        return repository.getRegisterRepository().userRegister(username, name, email, password)
    }
}