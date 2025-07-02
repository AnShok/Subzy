package com.anshok.subzy.presentation.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.databinding.ActivityRootBinding
import com.anshok.subzy.util.ads.AdManager

class RootActivity : AppCompatActivity() {

    private val binding: ActivityRootBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val prefs: UserPreferences =
            org.koin.java.KoinJavaComponent.get(UserPreferences::class.java)
        prefs.incrementAppLaunchCount()

        val isPro = AppFeaturesManager.isProActive()
        val appLaunchCount = prefs.getAppLaunchCount()

        AdManager.showOnAppLaunchIfEligible(
            activity = this,
            isPro = isPro,
            appLaunchCount = appLaunchCount,
            onDismiss = {}
        )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.settingsFragment, R.id.calendarFragment -> {
                    // Установка цвета статус-бара для SpendFragment и WalletFragment
                    setStatusBarColor(R.color.Gray_80)
                }

                R.id.addSubSearchFragment, R.id.addSubCreateFragment, R.id.detailsSubFragment -> {
                    // Установка цвета статус-бара для SettingsFragment и NewSubscriptionFragment
                    setStatusBarColor(R.color.Gray_75)
                }

                else -> {
                    // Установка стандартного цвета статус-бара
                    setStatusBarColor(R.color.Gray_80)
                }
            }
        }
    }

    // Метод для установки цвета статус-бара
    private fun setStatusBarColor(colorResId: Int) {
        window.statusBarColor = ContextCompat.getColor(this, colorResId)
    }

    // Зглушка, про не про
    object AppFeaturesManager {
        fun isProActive(): Boolean {
            return true
        }
    }

}