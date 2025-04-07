package com.anshok.subzy.data.remote.logo

import com.anshok.subzy.data.remote.logo.dto.LogoResponse
import retrofit2.Response

interface LogoRemoteDataSource {
    suspend fun searchCompany(query: String): Response<List<LogoResponse>>
}