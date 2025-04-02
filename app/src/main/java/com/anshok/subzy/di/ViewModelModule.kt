package com.anshok.subzy.di

import com.anshok.subzy.presentation.add_sub.search.AddSubSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        AddSubSearchViewModel(
            searchInteractor = get(), get()
        )
    }
}