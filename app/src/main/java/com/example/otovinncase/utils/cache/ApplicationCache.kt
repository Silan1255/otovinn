package com.example.otovinncase.utils.cache

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class ApplicationCache @Inject constructor(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(CacheKey.CACHE_KEY, Context.MODE_PRIVATE)

    fun setUserToken(userToken: String?) {
        sharedPreferences.edit().putString(CacheKey.USER_TOKEN, userToken).apply()
    }

    fun getUserToken(): String? {
        return sharedPreferences.getString(CacheKey.USER_TOKEN, null)
    }

}