package com.anshok.subzy.domain.impl

import com.anshok.subzy.domain.api.SearchInteractor
import com.anshok.subzy.domain.api.SearchRepository
import com.anshok.subzy.domain.model.Logo
import com.anshok.subzy.util.ResponseData

class SearchInteractorImpl(
    private val repository: SearchRepository
) : SearchInteractor {

    override suspend fun searchCompany(query: String): ResponseData<Logo> {
        return repository.searchCompany(query)
    }
}
