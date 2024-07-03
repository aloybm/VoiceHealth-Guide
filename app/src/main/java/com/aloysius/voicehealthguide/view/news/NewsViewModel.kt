package com.aloysius.voicehealthguide.view.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.response.ArticlesItem
import com.aloysius.voicehealthguide.data.remote.response.NewsResponse
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: UserRepository) : ViewModel() {
    fun getListNews(): LiveData<ResultState<NewsResponse>>{
        return repository.getNewsRepository().getNews()

    }
}
