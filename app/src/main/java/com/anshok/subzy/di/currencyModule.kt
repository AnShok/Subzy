package com.anshok.subzy.di

import com.anshok.subzy.data.remote.currency.search.CbrNetworkClient
import com.anshok.subzy.data.repository.impl.CurrencyRepositoryImpl
import com.anshok.subzy.domain.api.CurrencyInteractor
import com.anshok.subzy.domain.api.CurrencyRepository
import com.anshok.subzy.domain.impl.CurrencyInteractorImpl
import com.anshok.subzy.presentation.settings.viewmodel.CurrencyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val currencyModule = module {
    single { CbrNetworkClient.provideCbrApi() }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get()) }
    single<CurrencyInteractor> { CurrencyInteractorImpl(get()) }
    viewModel {
        CurrencyViewModel(
            currencyInteractor = get(),
            preferences = get(),
            notifier = get()
        )
    }

}
