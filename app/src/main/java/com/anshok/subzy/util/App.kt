package com.anshok.subzy.util

import android.app.Application
import com.anshok.subzy.di.currencyModule
import com.anshok.subzy.di.dataModule
import com.anshok.subzy.di.interactorModule
import com.anshok.subzy.di.repositoryModule
import com.anshok.subzy.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                viewModelModule,
                interactorModule,
                repositoryModule,
                dataModule,
                currencyModule
            )
        }

        // Вся инициализация — в AppInitializer
        AppInitializer(
            context = this,
            prefs = getKoin().get(),
            currencyInteractor = getKoin().get()
        ).init()
    }
}


