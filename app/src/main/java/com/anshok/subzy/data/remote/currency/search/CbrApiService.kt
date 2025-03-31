package com.anshok.subzy.data.remote.currency.search

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CbrApiService {
    @GET("scripts/XML_daily.asp")
    suspend fun getRates(@Query("date_req") date: String? = null): Response<ResponseBody>
}
