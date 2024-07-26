package com.example.otovinncase.utils

import com.example.otovinncase.ui.login.view.currentContext
import com.example.otovinncase.utils.cache.ApplicationCache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object Utils {
    private var loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    var client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .addInterceptor(HeaderInterceptor(ApplicationCache(currentContext)))
        .addInterceptor(loggingInterceptor)
        .build()

}