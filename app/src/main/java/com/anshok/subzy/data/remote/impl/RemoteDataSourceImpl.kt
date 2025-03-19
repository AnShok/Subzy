package com.anshok.subzy.data.remote.impl

import com.anshok.subzy.data.remote.RemoteDataSource
import com.anshok.subzy.data.remote.search.dto.LogoResponse
import com.anshok.subzy.data.remote.search.network.LogoApiService
import retrofit2.Response

class RemoteDataSourceImpl(
    private val logoApiService: LogoApiService
) : RemoteDataSource {

    override suspend fun searchCompany(query: String, apiKey: String): Response<LogoResponse> {
        return logoApiService.searchCompany(query, apiKey)
    }
}