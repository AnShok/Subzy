package com.anshok.subzy.data.remote

import com.anshok.subzy.data.remote.search.dto.LogoResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun searchCompany(query: String, apiKey: String): Response<LogoResponse>
}