package com.anshok.subzy.data.remote.search.network

import android.content.Context
import com.anshok.subzy.data.NetworkClient
import com.anshok.subzy.data.remote.search.dto.LogoResponse
import com.anshok.subzy.data.remote.search.dto.RESULT_CODE_NO_INTERNET
import com.anshok.subzy.data.remote.search.dto.RESULT_CODE_SERVER_ERROR
import com.anshok.subzy.data.remote.search.dto.RESULT_CODE_SUCCESS
import com.anshok.subzy.util.isInternetAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RetrofitNetworkClient(
    private val logoApiService: LogoApiService,
    private val context: Context
) : NetworkClient {

    override suspend fun searchLogos(query: String): LogoResponse {
        if (!context.isInternetAvailable()) {
            return LogoResponse().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = logoApiService.searchCompany(query)
                if (response.isSuccessful) {
                    response.body()?.apply { resultCode = RESULT_CODE_SUCCESS }
                        ?: LogoResponse().apply { resultCode = RESULT_CODE_SERVER_ERROR }
                } else {
                    LogoResponse().apply { resultCode = response.code() }
                }
            } catch (e: HttpException) {
                LogoResponse().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }
}