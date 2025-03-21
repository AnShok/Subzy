package com.anshok.subzy.domain.api

import com.anshok.subzy.domain.model.Logo
import com.anshok.subzy.util.ResponseData

interface SearchInteractor {
    suspend fun searchCompany(query: String): ResponseData<Logo>
}
