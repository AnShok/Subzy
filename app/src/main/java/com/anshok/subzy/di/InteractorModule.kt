package com.anshok.subzy.di

import com.anshok.subzy.domain.category.CategoryInteractor
import com.anshok.subzy.domain.category.CategoryInteractorImpl
import com.anshok.subzy.domain.currency.CurrencyInteractor
import com.anshok.subzy.domain.currency.CurrencyInteractorImpl
import com.anshok.subzy.domain.help.HelpInteractor
import com.anshok.subzy.domain.help.HelpInteractorImpl
import com.anshok.subzy.domain.logo.LocalLogoInteractor
import com.anshok.subzy.domain.logo.LocalLogoInteractorImpl
import com.anshok.subzy.domain.paymentMethod.PaymentMethodInteractor
import com.anshok.subzy.domain.paymentMethod.PaymentMethodInteractorImpl
import com.anshok.subzy.domain.rate.RateInteractor
import com.anshok.subzy.domain.rate.RateInteractorImpl
import com.anshok.subzy.domain.search.SearchInteractor
import com.anshok.subzy.domain.search.SearchInteractorImpl
import com.anshok.subzy.domain.settings.SettingsInteractor
import com.anshok.subzy.domain.settings.SettingsInteractorImpl
import com.anshok.subzy.domain.subscription.SubscriptionInteractor
import com.anshok.subzy.domain.subscription.SubscriptionInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {

    // Интерактор работы с подписками
    factory<SubscriptionInteractor> {
        SubscriptionInteractorImpl(repository = get())
    }

    // Интерактор работы с категориями
    factory<CategoryInteractor> {
        CategoryInteractorImpl(repository = get())
    }

    // Интерактор для методов оплаты (карты и пр.)
    factory<PaymentMethodInteractor> {
        PaymentMethodInteractorImpl(repository = get())
    }

    // Интерактор поиска логотипов (по API logo.dev)
    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }

    // Интерактор поиска логотипов только среди локальных ресурсов
    factory<LocalLogoInteractor> {
        LocalLogoInteractorImpl()
    }

    // Интерактор экрана помощи (email и Telegram)
    factory<HelpInteractor> { HelpInteractorImpl(get()) }

    // Интерактор экрана оценки приложения (Google Play Intent)
    factory<RateInteractor> { RateInteractorImpl(get()) }

    // Интерактор работы с валютами (загрузка, кэш, дефолт)
    factory<CurrencyInteractor> {
        CurrencyInteractorImpl(repository = get())
    }

    // Интерактор экрана настроек
    factory<SettingsInteractor> {
        SettingsInteractorImpl(
            repository = get(),
            context = androidContext()
        )
    }

}
