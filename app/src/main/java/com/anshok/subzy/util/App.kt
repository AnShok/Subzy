package com.anshok.subzy.util

import android.app.Application
import android.util.Log
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
                dataModule
            )
        }

        // Вся инициализация — в AppInitializer
        AppInitializer(
            context = this,
            prefs = getKoin().get(),
            currencyInteractor = getKoin().get()
        ).init()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("GlobalException", "Непойманная ошибка: ${throwable.message}", throwable)
            // подключить Firebase / Crashlytics
        }
    }
}


