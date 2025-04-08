package com.anshok.subzy.di

import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.data.repository.impl.SettingsRepositoryImpl
import com.anshok.subzy.domain.help.HelpInteractor
import com.anshok.subzy.domain.settings.SettingsRepository
import com.anshok.subzy.presentation.addSub.create.AddSubCreateViewModel
import com.anshok.subzy.presentation.addSub.search.AddSubSearchViewModel
import com.anshok.subzy.presentation.calendar.CalendarViewModel
import com.anshok.subzy.presentation.home.viewmodel.MySubViewModel
import com.anshok.subzy.presentation.settings.viewmodel.AboutUsViewModel
import com.anshok.subzy.presentation.settings.viewmodel.AppIconViewModel
import com.anshok.subzy.presentation.settings.viewmodel.CurrencyViewModel
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

    // ViewModel для экрана поиска сервиса (добавление подписки)
    viewModel {
        AddSubSearchViewModel(
            searchInteractor = get(),
            localLogoInteractor = get()
        )
    }

    // ViewModel для экрана добавления подписки (форма)
    viewModel {
        AddSubCreateViewModel(
            subscriptionInteractor = get(),
            currencyInteractor = get(),
            userPreferences = get()
        )
    }

    // ViewModel для главного экрана (подписки + метрики)
    viewModel {
        MySubViewModel(
            subscriptionInteractor = get(),
            currencyInteractor = get(),
            userPreferences = get(),
            notifier = get()
        )
    }

    // ViewModel для детального экрана подписки
    viewModel { DetailsSubViewModel(get()) }

    // ViewModel для выбора валюты в настройках
    viewModel {
        CurrencyViewModel(
            currencyInteractor = get(),
            preferences = get(),
            notifier = get()
        )
    }

    // ViewModel для настроек
    viewModel {
        SettingsViewModel(
            interactor = get()
        )
    }

    // ViewModel для смены иконки приложения
    viewModel { AppIconViewModel(get(), get()) }

    // ViewModel для смены темы приложения
    viewModel { ThemeViewModel(get()) }

    // ViewModel для раздела "Помощь"
    viewModel { HelpViewModel(get<HelpInteractor>()) }

    // ViewModel для экрана оценки
    //viewModel { RateViewModel(get(), get()) } // get() = RateInteractor, HelpInteractor
    viewModel {
        RateViewModel(
            rateInteractor = get(),
            helpInteractor = get()
        )
    }

    // ViewModel для экрана "О нас"
    viewModel { AboutUsViewModel(get()) }

    // ViewModel для пасхалки (Easter Egg)
    viewModel { EasterEggViewModel() }

    // Preferences + SettingsRepository
    single { UserPreferences(androidContext()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    // EventBus для обновлений валюты (через SharedFlow)
    single { CurrencyChangedNotifier() }

    viewModel {
        CalendarViewModel(
            subscriptionInteractor = get(),
            userPreferences = get()
        )
    }

}

