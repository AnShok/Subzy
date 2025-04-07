package com.anshok.subzy.di

import com.anshok.subzy.data.repository.impl.CategoryRepositoryImpl
import com.anshok.subzy.data.repository.impl.CurrencyRepositoryImpl
import com.anshok.subzy.data.repository.impl.HelpRepositoryImpl
import com.anshok.subzy.data.repository.impl.PaymentMethodRepositoryImpl
import com.anshok.subzy.data.repository.impl.RateRepositoryImpl
import com.anshok.subzy.data.repository.impl.SearchRepositoryImpl
import com.anshok.subzy.data.repository.impl.SubscriptionRepositoryImpl
import com.anshok.subzy.domain.category.CategoryRepository
import com.anshok.subzy.domain.currency.CurrencyRepository
import com.anshok.subzy.domain.help.HelpRepository
import com.anshok.subzy.domain.paymentMethod.PaymentMethodRepository
import com.anshok.subzy.domain.rate.RateRepository
import com.anshok.subzy.domain.search.SearchRepository
import com.anshok.subzy.domain.subscription.SubscriptionRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    // Репозиторий подписок (работает с Room через LocalDataSource)
    single<SubscriptionRepository> {
        SubscriptionRepositoryImpl(localDataSource = get())
    }

    // Репозиторий категорий (Room)
    single<CategoryRepository> {
        CategoryRepositoryImpl(localDataSource = get())
    }

    // Репозиторий методов оплаты (Room)
    single<PaymentMethodRepository> {
        PaymentMethodRepositoryImpl(localDataSource = get())
    }

    // Репозиторий поиска логотипов (работает напрямую с LogoApiService)
    single<SearchRepository> {
        SearchRepositoryImpl(logoApiService = get())
    }

    // Репозиторий помощи (email/Telegram интенты)
    factory<HelpRepository> {
        HelpRepositoryImpl(androidContext())
    }

    // Репозиторий для оценки в Google Play (Intent)
    single<RateRepository> {
        RateRepositoryImpl(androidContext())
    }

    // Репозиторий курсов валют (работает с CBR API + SharedPreferences)
    single<CurrencyRepository> {
        CurrencyRepositoryImpl(
            api = get(),         // CbrApiService
            prefs = get(),       // UserPreferences
            gson = get()         // Gson
        )
    }

}
