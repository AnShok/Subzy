package com.anshok.subzy.util

import android.app.Application
import com.anshok.subzy.data.local.impl.EmbeddedLogoProvider
import com.anshok.subzy.di.dataModule
import com.anshok.subzy.di.interactorModule
import com.anshok.subzy.di.repositoryModule
import com.anshok.subzy.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        EmbeddedLogoProvider.init(this)
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