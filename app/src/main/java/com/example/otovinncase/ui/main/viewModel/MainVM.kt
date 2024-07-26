package com.example.otovinncase.ui.main.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otovinncase.data.model.BaseResponse
import com.example.otovinncase.data.model.Discover
import com.example.otovinncase.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(private val repository: ApiRepository) : ViewModel() {
    var discoverData: MutableLiveData<BaseResponse<Discover>> = MutableLiveData()

    fun getDiscover() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getDiscover()
            discoverData.postValue(response)
        }
    }

}