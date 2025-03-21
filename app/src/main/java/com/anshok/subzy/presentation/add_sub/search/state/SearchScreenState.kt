package com.anshok.subzy.presentation.add_sub.search.state

import com.anshok.subzy.domain.model.Subscription
import com.anshok.subzy.util.ResponseData.ResponseError

sealed interface SearchScreenState {

    data object Loading : SearchScreenState

    data object LoadingNextPage : SearchScreenState
    data class LoadNextPage(
        val subscriptions: List<Subscription>
    ) : SearchScreenState

    data object Default : SearchScreenState

    data object NothingFound : SearchScreenState

    data class Success(
        val subscriptions: List<Subscription>,
        val found: Int
    ) : SearchScreenState

    data class Error(
        val error: ResponseError
    ) : SearchScreenState

    data class ErrorNextPage(
        val error: ResponseError
    ) : SearchScreenState
}
