package com.anshok.subzy.domain.search

import com.anshok.subzy.domain.logo.model.Logo
import com.anshok.subzy.util.ResourceLogo

interface SearchInteractor {
    suspend fun searchCompany(query: String): ResourceLogo<List<Logo>>
}
