package com.anshok.subzy.data.repository.impl

import android.util.Log
import com.anshok.subzy.data.converters.EmbeddedLogoMapper
import com.anshok.subzy.data.converters.EntityToDomainMapper
import com.anshok.subzy.data.local.impl.EmbeddedLogoProvider
import com.anshok.subzy.data.remote.logo.search.network.LogoApiService
import com.anshok.subzy.domain.api.SearchRepository
import com.anshok.subzy.domain.model.Logo
import com.anshok.subzy.util.ResourceLogo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class SearchRepositoryImpl(
    private val logoApiService: LogoApiService
) : SearchRepository {

    override suspend fun searchCompany(query: String): ResourceLogo<List<Logo>> {
        return withContext(Dispatchers.IO) {
            try {
                // Локальные результаты
                val localResults = EmbeddedLogoMapper.mapToDomainList(
                    EmbeddedLogoProvider.search(query)
                )
                Log.d("SearchRepository", "Local results: $localResults")

                // API запрос
                val response = logoApiService.searchCompany(query)
                val apiResults = if (response.isSuccessful) {
                    response.body()?.map { EntityToDomainMapper.logo(it) } ?: emptyList()
                } else {
                    Log.e("API_RESPONSE", "API Error: HTTP ${response.code()}")
                    return@withContext handleApiError(response.code())
                }

                Log.d("SearchRepository", "API results: $apiResults")

                // Объединение и удаление дублей
                val combinedResults = (localResults + apiResults)
                    .distinctBy { it.name?.lowercase() ?: "" }

                Log.d("SearchRepository", "Combined results: $combinedResults")

                // Возврат результата
                if (combinedResults.isEmpty()) {
                    ResourceLogo.Error(ResourceLogo.ResponseError.NOT_FOUND, "No results found")
                } else {
                    ResourceLogo.Success(combinedResults)
                }

            } catch (e: IOException) {
                Log.e("SearchRepository", "No Internet Connection", e)
                ResourceLogo.Error(ResourceLogo.ResponseError.NO_INTERNET, "No Internet connection")
            } catch (e: Exception) {
                Log.e("SearchRepository", "Unexpected Error", e)
                ResourceLogo.Error(ResourceLogo.ResponseError.UNKNOWN_ERROR, e.localizedMessage)
            }
        }
    }

    private fun handleApiError(code: Int): ResourceLogo<List<Logo>> {
        Log.e("SearchRepository", "API Error: $code")
        return when (code) {
            in 400..499 -> ResourceLogo.Error(
                ResourceLogo.ResponseError.CLIENT_ERROR,
                "Client Error: $code"
            )

            in 500..599 -> ResourceLogo.Error(
                ResourceLogo.ResponseError.SERVER_ERROR,
                "Server Error: $code"
            )

            else -> ResourceLogo.Error(
                ResourceLogo.ResponseError.UNKNOWN_ERROR,
                "Unexpected Error: $code"
            )
        }
    }
}
