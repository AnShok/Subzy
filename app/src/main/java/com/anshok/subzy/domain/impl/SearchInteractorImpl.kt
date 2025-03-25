package com.anshok.subzy.domain.impl

import android.util.Log
import com.anshok.subzy.data.converters.EmbeddedLogoMapper
import com.anshok.subzy.data.local.impl.EmbeddedLogoProvider
import com.anshok.subzy.domain.api.SearchInteractor
import com.anshok.subzy.domain.api.SearchRepository
import com.anshok.subzy.domain.model.Logo
import com.anshok.subzy.util.ResourceLogo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchInteractorImpl(
    private val repository: SearchRepository
) : SearchInteractor {

    override suspend fun searchCompany(query: String): ResourceLogo<List<Logo>> {
        return withContext(Dispatchers.IO) {
            try {
                // 🔍 Локальные совпадения
                val localResults = EmbeddedLogoMapper.mapToDomainList(
                    EmbeddedLogoProvider.search(query)
                )

                // 🌐 Запрос к API
                val apiResponse = repository.searchCompany(query)

                // 🎯 API успешен
                val apiResults = when (apiResponse) {
                    is ResourceLogo.Success -> apiResponse.data
                    is ResourceLogo.Error -> {
                        Log.e("SearchInteractor", "API Error: ${apiResponse.message}")
                        return@withContext if (localResults.isNotEmpty()) {
                            ResourceLogo.Success(localResults)
                        } else {
                            apiResponse // Возврат ошибки, если и локальных нет
                        }
                    }
                }

                // 🧠 Объединяем без дублей
                val combinedResults = (localResults + apiResults)
                    .distinctBy { it.name?.lowercase() ?: "" }

                return@withContext if (combinedResults.isNotEmpty()) {
                    ResourceLogo.Success(combinedResults)
                } else {
                    ResourceLogo.Error(ResourceLogo.ResponseError.NOT_FOUND, "Ничего не найдено")
                }

            } catch (e: Exception) {
                Log.e("SearchInteractor", "Unexpected Error", e)
                ResourceLogo.Error(ResourceLogo.ResponseError.UNKNOWN_ERROR, e.localizedMessage)
            }
        }
    }
}
