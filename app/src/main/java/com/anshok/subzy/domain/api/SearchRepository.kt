package com.anshok.subzy.domain.api

import com.anshok.subzy.domain.model.Logo
import com.anshok.subzy.util.ResourceLogo

interface SearchRepository {
    suspend fun searchCompany(query: String): ResourceLogo<Logo>
}
