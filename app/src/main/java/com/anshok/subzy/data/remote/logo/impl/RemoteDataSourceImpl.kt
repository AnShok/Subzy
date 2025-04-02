package com.anshok.subzy.data.remote.logo.impl

import android.util.Log
import com.anshok.subzy.data.remote.logo.RemoteDataSource
import com.anshok.subzy.data.remote.logo.search.dto.LogoResponse
import com.anshok.subzy.data.remote.logo.search.network.LogoApiService
import retrofit2.Response

class RemoteDataSourceImpl(
    private val logoApiService: LogoApiService
) : RemoteDataSource {

    override suspend fun searchCompany(query: String): Response<List<LogoResponse>> {
        Log.d("API_REQUEST", "Sending request: query=$query")

        return logoApiService.searchCompany(query)
    }
}