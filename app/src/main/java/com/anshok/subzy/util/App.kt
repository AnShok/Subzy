package com.anshok.subzy.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.anshok.subzy.data.local.UserPreferences
import com.anshok.subzy.data.local.impl.EmbeddedLogoProvider
import com.anshok.subzy.di.currencyModule
import com.anshok.subzy.di.dataModule
import com.anshok.subzy.di.interactorModule
import com.anshok.subzy.di.repositoryModule
import com.anshok.subzy.di.viewModelModule
import com.anshok.subzy.domain.model.AppTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

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
                dataModule,
                currencyModule
            )
        }

        val prefs = getKoin().get<UserPreferences>()

        // ✅ Применяем выбранную тему при старте приложения
        when (prefs.getAppTheme()) {
            AppTheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AppTheme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        // Применяем стиль иконки
        switchAppIcon(this, prefs.getAppIconStyle())
    }

}
