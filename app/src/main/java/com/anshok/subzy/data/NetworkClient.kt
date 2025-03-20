package com.anshok.subzy.data

import com.anshok.subzy.data.remote.search.dto.LogoResponse

interface NetworkClient {
    suspend fun searchLogos(query: String): LogoResponse
}