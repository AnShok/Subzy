package com.anshok.subzy.data.repository.impl

import com.anshok.subzy.data.converters.EntityToDomainMapper
import com.anshok.subzy.data.remote.search.network.LogoApiService
import com.anshok.subzy.domain.api.SearchRepository
import com.anshok.subzy.domain.model.Logo
import com.anshok.subzy.util.ResourceLogo
import java.io.IOException

class SearchRepositoryImpl(
    private val logoApiService: LogoApiService
) : SearchRepository {

    override suspend fun searchCompany(query: String): ResourceLogo<Logo> {
        return try {
            val response = logoApiService.searchCompany(query)
            if (response.isSuccessful) {
                response.body()?.let {
                    ResourceLogo.Success(EntityToDomainMapper.logo(it))
                } ?: ResourceLogo.Error(ResourceLogo.ResponseError.NOT_FOUND, "Logo not found")
            } else {
                val errorType = when (response.code()) {
                    in 400..499 -> ResourceLogo.ResponseError.CLIENT_ERROR
                    in 500..599 -> ResourceLogo.ResponseError.SERVER_ERROR
                    else -> ResourceLogo.ResponseError.UNKNOWN_ERROR
                }
                ResourceLogo.Error(errorType, "HTTP Error: ${response.code()}")
            }
        } catch (e: IOException) {
            ResourceLogo.Error(ResourceLogo.ResponseError.NO_INTERNET, "No Internet connection")
        } catch (e: Exception) {
            ResourceLogo.Error(ResourceLogo.ResponseError.UNKNOWN_ERROR, e.localizedMessage)
        }
    }
}
