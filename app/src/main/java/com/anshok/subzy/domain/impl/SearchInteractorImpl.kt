package com.anshok.subzy.domain.impl

import com.anshok.subzy.domain.api.SearchInteractor
import com.anshok.subzy.domain.api.SearchRepository
import com.anshok.subzy.domain.model.Logo
import com.anshok.subzy.util.ResourceLogo

class SearchInteractorImpl(
    private val repository: SearchRepository
) : SearchInteractor {

    override suspend fun searchCompany(query: String): ResourceLogo<Logo> {
        return repository.searchCompany(query)
    }
}
