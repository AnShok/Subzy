package com.anshok.subzy.di

import com.anshok.subzy.data.repository.impl.CategoryRepositoryImpl
import com.anshok.subzy.data.repository.impl.PaymentMethodRepositoryImpl
import com.anshok.subzy.data.repository.impl.SearchRepositoryImpl
import com.anshok.subzy.data.repository.impl.SubscriptionRepositoryImpl
import com.anshok.subzy.domain.api.CategoryRepository
import com.anshok.subzy.domain.api.PaymentMethodRepository
import com.anshok.subzy.domain.api.SearchRepository
import com.anshok.subzy.domain.api.SubscriptionRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<SubscriptionRepository> {
        SubscriptionRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get()
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
}
