package com.anshok.subzy.data.remote.interceptors

import android.util.Log
import com.anshok.subzy.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

object HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.LOGO_SECRET_KEY}")
            .build()

        Log.d("API_REQUEST", "Authorization: Bearer ${BuildConfig.LOGO_SECRET_KEY.take(6)}****")

        return chain.proceed(request)
    }
}