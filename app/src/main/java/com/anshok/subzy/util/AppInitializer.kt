package com.anshok.subzy.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.data.local.impl.EmbeddedLogoProvider
import com.anshok.subzy.domain.currency.CurrencyInteractor
import com.anshok.subzy.domain.settings.model.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppInitializer(
    private val context: Context,
    private val prefs: UserPreferences,
    private val currencyInteractor: CurrencyInteractor
) {
    fun init() {
        initEmbeddedLogos()
        initCurrencies()
        applyTheme()
        applyAppIcon()
    }

    private fun initEmbeddedLogos() {
        EmbeddedLogoProvider.init(context)
    }

    private fun initCurrencies() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                currencyInteractor.loadCurrencies()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun applyTheme() {
        when (prefs.getAppTheme()) {
            AppTheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AppTheme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun applyAppIcon() {
        switchAppIcon(context, prefs.getAppIconStyle())
    }
}

