package com.anshok.subzy.data.repository.impl

import com.anshok.subzy.data.converters.EntityToDomainMapper
import com.anshok.subzy.data.remote.search.network.LogoApiService
import com.anshok.subzy.domain.api.SearchRepository
import com.anshok.subzy.domain.model.Logo
import com.anshok.subzy.util.ResponseData
import java.io.IOException

class SearchRepositoryImpl(
    private val logoApiService: LogoApiService
) : SearchRepository {

    override suspend fun searchCompany(query: String): ResponseData<Logo> {
        return try {
            val response = logoApiService.searchCompany(query)
            if (response.isSuccessful) {
                response.body()?.let {
                    ResponseData.Success(EntityToDomainMapper.logo(it))
                } ?: ResponseData.Error(ResponseData.ResponseError.NOT_FOUND, "Logo not found")
            } else {
                val errorType = when (response.code()) {
                    in 400..499 -> ResponseData.ResponseError.CLIENT_ERROR
                    in 500..599 -> ResponseData.ResponseError.SERVER_ERROR
                    else -> ResponseData.ResponseError.UNKNOWN_ERROR
                }
                ResponseData.Error(errorType, "HTTP Error: ${response.code()}")
            }
        } catch (e: IOException) {
            ResponseData.Error(ResponseData.ResponseError.NO_INTERNET, "No Internet connection")
        } catch (e: Exception) {
            ResponseData.Error(ResponseData.ResponseError.UNKNOWN_ERROR, e.localizedMessage)
        }
    }
}
