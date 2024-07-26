package com.example.otovinncase.data.repository

import com.example.otovinncase.data.api.ApiService
import com.example.otovinncase.data.model.BaseResponse
import com.example.otovinncase.data.model.Discover
import com.example.otovinncase.data.model.LoginRequestModel
import com.example.otovinncase.data.model.LoginResponseModel
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(loginRequestModel: LoginRequestModel): LoginResponseModel {
        return apiService.login(loginRequestModel)
    }

    suspend fun getDiscover(): BaseResponse<Discover> {
        return apiService.getDiscover()
    }
}