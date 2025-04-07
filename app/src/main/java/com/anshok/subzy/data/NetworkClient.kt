package com.anshok.subzy.data

import com.anshok.subzy.data.remote.logo.dto.LogoResponse

interface NetworkClient {
    suspend fun searchLogos(query: String): List<LogoResponse>
}