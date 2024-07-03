package com.aloysius.voicehealthguide.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aloysius.voicehealthguide.data.pref.UserModel
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> {
        return repository.getLoginRepository().userLogin(email, password)
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveSession(user)
            }
        }
    }
}
