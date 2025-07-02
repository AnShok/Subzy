package com.anshok.subzy.util.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.RequestConfiguration

object AdManager {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    //private const val TAG = "AdManager"
    private const val AD_UNIT_ID = "ca-app-pub-5915773850362568/6016734072" //тестовая "ca-app-pub-3940256099942544/1033173712"

    fun init(context: Context, isDebug: Boolean = false) {
        if (isDebug) {
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf("658C2CD509B8ABDB5E1E9DF19B0511F3"))
                    .build()
            )
        }
        MobileAds.initialize(context)
        loadAd(context)
    }

    private fun loadAd(context: Context) {
        if (isLoading || interstitialAd != null) return
        isLoading = true

        InterstitialAd.load(
            context,
            AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    //Log.d(TAG, "Интерстициальная реклама загружена.")
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    //Log.e(TAG, "Не удалось загрузить рекламу: ${error.message}")
                    interstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    fun showOnAppLaunchIfEligible(
        activity: Activity,
        isPro: Boolean,
        appLaunchCount: Int,
        onDismiss: () -> Unit
    ) {
        if (isPro || appLaunchCount % 3 != 0) {
            //Log.d(TAG, "Реклама при запуске не показана: Pro или не 3-й запуск.")
            onDismiss()
            return
        }

        show(activity, onDismiss)
    }

    fun showOnCalendarExitIfEligible(
        activity: Activity,
        isPro: Boolean,
        calendarExitCount: Int,
        onDismiss: () -> Unit
    ) {
        if (isPro || calendarExitCount % 2 != 0) {
            //Log.d(TAG, "Реклама при выходе из календаря не показана: Pro или не 2-й выход.")
            onDismiss()
            return
        }

        show(activity, onDismiss)
    }

    private fun show(activity: Activity, onDismiss: () -> Unit) {
        interstitialAd?.apply {
            fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    //Log.d(TAG, "Реклама закрыта.")
                    interstitialAd = null
                    loadAd(activity)
                    onDismiss()
                }

                override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                    //Log.e(TAG, "Ошибка показа рекламы: ${error.message}")
                    interstitialAd = null
                    loadAd(activity)
                    onDismiss()
                }
            }
            //Log.d(TAG, "Показ рекламы.")
            show(activity)
        } ?: onDismiss()
    }
}
