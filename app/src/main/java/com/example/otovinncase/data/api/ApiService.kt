package com.example.otovinncase.data.api

import com.example.otovinncase.data.model.BaseResponse
import com.example.otovinncase.data.model.Discover
import com.example.otovinncase.data.model.LoginRequestModel
import com.example.otovinncase.data.model.LoginResponseModel
import com.example.otovinncase.utils.Constants.Companion.BASE_URL
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST(BASE_URL + "login")
    suspend fun login(@Body loginRequestEnvelope: LoginRequestModel): LoginResponseModel

    @GET(BASE_URL + "discover")
    suspend fun getDiscover(): BaseResponse<Discover>
}