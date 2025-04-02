package com.anshok.subzy.data.remote.currency.search

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object CbrNetworkClient {
    fun provideCbrApi(): CbrApiService = Retrofit.Builder()
        .baseUrl("https://www.cbr.ru/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(CbrApiService::class.java)
}
