package com.anshok.subzy.data.remote.search.network

import com.anshok.subzy.data.remote.search.dto.LogoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LogoApiService {
    @GET("search")
    suspend fun searchCompany(
        @Query("q") query: String,
        //@Header("Authorization") token: String = "Bearer ${BuildConfig.LOGO_SECRET_KEY}"
    ): Response<List<LogoResponse>>
}
