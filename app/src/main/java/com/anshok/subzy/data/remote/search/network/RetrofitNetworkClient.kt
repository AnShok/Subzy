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

    override suspend fun searchLogos(query: String): List<LogoResponse> {
        if (!context.isInternetAvailable()) {
            return listOf(LogoResponse(name = null, domain = null, logoUrl = null, resultCode = RESULT_CODE_NO_INTERNET))
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = logoApiService.searchCompany(query)
                if (response.isSuccessful) {
                    response.body()?.map { it.copy(resultCode = RESULT_CODE_SUCCESS) }
                        ?: listOf(LogoResponse(name = null, domain = null, logoUrl = null, resultCode = RESULT_CODE_SERVER_ERROR))
                } else {
                    listOf(LogoResponse(name = null, domain = null, logoUrl = null, resultCode = response.code()))
                }
            } catch (e: HttpException) {
                listOf(LogoResponse(name = null, domain = null, logoUrl = null, resultCode = RESULT_CODE_SERVER_ERROR))
            }
        }
    }
}
