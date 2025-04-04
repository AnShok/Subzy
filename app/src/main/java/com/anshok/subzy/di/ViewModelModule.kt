package com.anshok.subzy.di

import com.anshok.subzy.data.local.UserPreferences
import com.anshok.subzy.data.repository.impl.SettingsRepositoryImpl
import com.anshok.subzy.domain.api.HelpInteractor
import com.anshok.subzy.domain.api.SettingsRepository
import com.anshok.subzy.presentation.addSub.create.AddSubCreateViewModel
import com.anshok.subzy.presentation.addSub.search.AddSubSearchViewModel
import com.anshok.subzy.presentation.mySub.viewmodel.MySubViewModel
import com.anshok.subzy.presentation.settings.viewmodel.AboutUsViewModel
import com.anshok.subzy.presentation.settings.viewmodel.AppIconViewModel
import com.anshok.subzy.presentation.settings.viewmodel.EasterEggViewModel
import com.anshok.subzy.presentation.settings.viewmodel.HelpViewModel
import com.anshok.subzy.presentation.settings.viewmodel.RateViewModel
import com.anshok.subzy.presentation.settings.viewmodel.SettingsViewModel
import com.anshok.subzy.presentation.settings.viewmodel.ThemeViewModel
import com.anshok.subzy.presentation.subDetails.DetailsSubViewModel
import com.anshok.subzy.shared.events.CurrencyChangedNotifier
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        AddSubSearchViewModel(
            searchInteractor = get(), get()
        )
    }

    viewModel {
        SettingsViewModel(
            repository = get(),
            appContext = androidContext()
        )
    }

    single { UserPreferences(androidContext()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    viewModel { AppIconViewModel(get(), get()) }
    viewModel { ThemeViewModel(get()) }

    viewModel { HelpViewModel(get<HelpInteractor>()) }

    viewModel { RateViewModel(get(), get()) } // get() = RateInteractor, HelpInteractor

    viewModel { AboutUsViewModel(get()) }
    viewModel { EasterEggViewModel() }

    viewModel {
        MySubViewModel(
            subscriptionInteractor = get(),
            currencyInteractor = get(),
            userPreferences = get(),
            notifier = get()
        )
    }


    viewModel {
        AddSubCreateViewModel(
            subscriptionInteractor = get(),
            currencyInteractor = get(),
            userPreferences = get()
        )
    }

    single { CurrencyChangedNotifier() }

    viewModel { DetailsSubViewModel(get()) }


}

