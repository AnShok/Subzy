package com.anshok.subzy.di

import com.anshok.subzy.domain.api.CategoryInteractor
import com.anshok.subzy.domain.api.HelpInteractor
import com.anshok.subzy.domain.api.LocalLogoInteractor
import com.anshok.subzy.domain.api.PaymentMethodInteractor
import com.anshok.subzy.domain.api.RateInteractor
import com.anshok.subzy.domain.api.SearchInteractor
import com.anshok.subzy.domain.api.SubscriptionInteractor
import com.anshok.subzy.domain.impl.CategoryInteractorImpl
import com.anshok.subzy.domain.impl.HelpInteractorImpl
import com.anshok.subzy.domain.impl.LocalLogoInteractorImpl
import com.anshok.subzy.domain.impl.PaymentMethodInteractorImpl
import com.anshok.subzy.domain.impl.RateInteractorImpl
import com.anshok.subzy.domain.impl.SearchInteractorImpl
import com.anshok.subzy.domain.impl.SubscriptionInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<SubscriptionInteractor> {
        SubscriptionInteractorImpl(repository = get())
    }

    factory<CategoryInteractor> {
        CategoryInteractorImpl(repository = get())
    }

    factory<PaymentMethodInteractor> {
        PaymentMethodInteractorImpl(repository = get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }

    factory<LocalLogoInteractor> { LocalLogoInteractorImpl() }

    factory<HelpInteractor> { HelpInteractorImpl(get()) }

    factory<RateInteractor> { RateInteractorImpl(get()) }

}
