package com.aloysius.voicehealthguide.view.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.HistoryRepository
import com.aloysius.voicehealthguide.data.remote.response.ArticlesItem
import com.aloysius.voicehealthguide.data.remote.response.HistoryResponse
import com.aloysius.voicehealthguide.data.remote.response.RegisterResponse
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    fun getHistory(): LiveData<ResultState<HistoryResponse>> {
        return repository.getHistory()
    }

}
