package com.anshok.subzy.util

import android.app.Application
import com.anshok.subzy.di.dataModule
import com.anshok.subzy.di.interactorModule
import com.anshok.subzy.di.repositoryModule
import com.anshok.subzy.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

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
    }
}