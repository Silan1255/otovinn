package com.example.otovinncase.ui.login.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otovinncase.data.model.LoginRequestModel
import com.example.otovinncase.data.model.LoginResponseModel
import com.example.otovinncase.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    val loginData: MutableLiveData<LoginResponseModel> = MutableLiveData()

    fun login(loginRequestModel: LoginRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.login(loginRequestModel)
            loginData.postValue(response)
        }
    }
}