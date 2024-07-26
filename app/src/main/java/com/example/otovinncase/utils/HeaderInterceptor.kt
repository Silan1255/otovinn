package com.example.otovinncase.utils

import com.example.otovinncase.utils.cache.ApplicationCache
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private var cache: ApplicationCache) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val builder = chain.request().newBuilder()
        builder.addHeader("Authorization", "Bearer ${cache.getUserToken()?:""}")
        builder.addHeader("Content-Type", "application/json;charset=UTF-8")
        builder.addHeader("Accept", "application/vnd.api+json")

        return chain.proceed(builder.build())
    }
}