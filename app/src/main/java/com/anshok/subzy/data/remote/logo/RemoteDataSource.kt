package com.anshok.subzy.data.remote.logo

import com.anshok.subzy.data.remote.logo.search.dto.LogoResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun searchCompany(query: String): Response<List<LogoResponse>>
}