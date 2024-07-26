package com.example.otovinncase.data.model

data class BaseResponse<T>(
    val data: T,
    val message: String?,
    val code: Int?,
)