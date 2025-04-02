package com.anshok.subzy.presentation.addSub.search

import AddSubSearchState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.domain.api.LocalLogoInteractor
import com.anshok.subzy.domain.api.SearchInteractor
import com.anshok.subzy.domain.model.Logo
import com.anshok.subzy.util.ResourceLogo
import com.anshok.subzy.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddSubSearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val localLogoInteractor: LocalLogoInteractor
) : ViewModel() {

    private val _state =
        MutableLiveData<AddSubSearchState>(AddSubSearchState.LocalOnly(emptyList()))
    val state: LiveData<AddSubSearchState> = _state

    private val _logos = MutableLiveData<List<Logo>>(emptyList())
    val logos: LiveData<List<Logo>> = _logos

    private var currentQuery: String = ""
    val currentQueryText: String get() = currentQuery

    private var currentPage = 0
    private var maxPages = 1
    private var isNextPageLoading = false

    private var accumulatedResults: MutableList<Logo> = mutableListOf()

    private val debouncedSearch = debounce<String>(
        waitMs = 500L,
        coroutineScope = viewModelScope
    ) { query ->
        if (query.isBlank()) {
            showAllLocalData()
        } else {
            searchCombined(query, isNewRequest = true)
        }
    }

    fun onQueryChanged(query: String) {
        currentQuery = query
        if (query.isBlank()) {
            debouncedSearch.cancel()
            showAllLocalData()
        } else {
            debouncedSearch(query)
        }
    }

    fun onInitialLoad() {
        showAllLocalData()
    }

    fun onLastItemReached() {
        if (!isNextPageLoading && currentPage < maxPages) {
            isNextPageLoading = true
            _state.postValue(AddSubSearchState.LoadingNextPage)
            searchCombined(currentQuery, isNewRequest = false)
        } else if (!isNextPageLoading && currentPage >= maxPages) {
            _state.postValue(AddSubSearchState.NoMoreData)
        }
    }

    private fun showAllLocalData() {
        val allLocal = localLogoInteractor.getAllLogos()
        accumulatedResults = allLocal.toMutableList()
        _logos.postValue(allLocal)
        _state.postValue(AddSubSearchState.LocalOnly(allLocal))
    }

    private fun searchCombined(query: String, isNewRequest: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNewRequest) {
                _state.postValue(AddSubSearchState.Loading)
                _logos.postValue(emptyList())
                currentPage = 0
                accumulatedResults.clear()
            } else {
                currentPage++
            }

            val localResults = localLogoInteractor.getAllLogos().filter {
                it.name?.contains(query, ignoreCase = true) == true ||
                        it.domain?.contains(query, ignoreCase = true) == true
            }

            when (val result = searchInteractor.searchCompany(query)) {
                is ResourceLogo.Success -> {
                    val apiResults = result.data
                    val combined = (localResults + apiResults)
                        .distinctBy { it.name?.lowercase() ?: "" }

                    accumulatedResults = combined.toMutableList()
                    _logos.postValue(accumulatedResults)

                    maxPages = 1 // если API не поддерживает пагинацию
                    _state.postValue(
                        if (isNewRequest) AddSubSearchState.Success(accumulatedResults)
                        else AddSubSearchState.LoadNextPage(accumulatedResults)
                    )
                }

                is ResourceLogo.Error -> {
                    if (localResults.isNotEmpty()) {
                        accumulatedResults = localResults.toMutableList()
                        _logos.postValue(accumulatedResults)
                        _state.postValue(AddSubSearchState.LocalOnly(accumulatedResults))
                    } else {
                        _state.postValue(AddSubSearchState.Error(result.type, result.message))
                    }
                }
            }

            isNextPageLoading = false
        }
    }
}
