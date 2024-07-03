package com.aloysius.voicehealthguide.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aloysius.voicehealthguide.data.pref.UserModel
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.DrugRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val drugRepository: DrugRepository
) : ViewModel() {

    fun getDrugs(keluhan: String) = drugRepository.getDrugs(keluhan)
    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}
