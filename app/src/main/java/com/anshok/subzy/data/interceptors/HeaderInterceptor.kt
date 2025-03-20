package com.anshok.subzy.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import com.anshok.subzy.BuildConfig // Исправленный импорт

object HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.LOGO_API_KEY}") // Используем BuildConfig
            .build()

        return chain.proceed(request)
    }
}