package com.anshok.subzy.di

import com.anshok.subzy.data.repository.impl.CategoryRepositoryImpl
import com.anshok.subzy.data.repository.impl.HelpRepositoryImpl
import com.anshok.subzy.data.repository.impl.PaymentMethodRepositoryImpl
import com.anshok.subzy.data.repository.impl.RateRepositoryImpl
import com.anshok.subzy.data.repository.impl.SearchRepositoryImpl
import com.anshok.subzy.data.repository.impl.SubscriptionRepositoryImpl
import com.anshok.subzy.domain.api.CategoryRepository
import com.anshok.subzy.domain.api.HelpRepository
import com.anshok.subzy.domain.api.PaymentMethodRepository
import com.anshok.subzy.domain.api.RateRepository
import com.anshok.subzy.domain.api.SearchRepository
import com.anshok.subzy.domain.api.SubscriptionRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<SubscriptionRepository> {
        SubscriptionRepositoryImpl(
            localDataSource = get()
        )
    }

    single<CategoryRepository> {
        CategoryRepositoryImpl(localDataSource = get())
    }

    single<PaymentMethodRepository> {
        PaymentMethodRepositoryImpl(localDataSource = get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(logoApiService = get())
    }

    factory<HelpRepository> { HelpRepositoryImpl(androidContext()) }

    single<RateRepository> { RateRepositoryImpl(androidContext()) }

}
