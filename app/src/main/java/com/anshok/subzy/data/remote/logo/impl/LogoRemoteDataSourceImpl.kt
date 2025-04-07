package com.anshok.subzy.data.remote.logo.impl

import android.util.Log
import com.anshok.subzy.data.remote.logo.LogoRemoteDataSource
import com.anshok.subzy.data.remote.logo.dto.LogoResponse
import com.anshok.subzy.data.remote.logo.network.LogoApiService
import retrofit2.Response

class LogoRemoteDataSourceImpl(
    private val logoApiService: LogoApiService
) : LogoRemoteDataSource {

    override suspend fun searchCompany(query: String): Response<List<LogoResponse>> {
        Log.d("API_REQUEST", "Sending request: query=$query")

        return logoApiService.searchCompany(query)
    }
}