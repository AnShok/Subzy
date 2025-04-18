package com.anshok.subzy.data.local.preferences

import android.content.Context
import com.anshok.subzy.domain.settings.model.AppIconStyle
import com.anshok.subzy.domain.settings.model.AppTheme
import java.time.LocalDate

class UserPreferences(private val context: Context) {

    private val prefs = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    fun saveProfileImagePath(path: String) {
        prefs.edit().putString("profile_image_path", path).apply()
    }

    fun getProfileImagePath(): String? {
        return prefs.getString("profile_image_path", null)
    }

    fun saveUserName(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }

    fun getUserName(): String? {
        return prefs.getString("user_name", null)
    }

    fun saveDefaultCurrency(code: String) {
        prefs.edit().putString("default_currency_code", code).apply()
    }

    fun getDefaultCurrency(): String {
        val saved = prefs.getString("default_currency_code", null)
        return if (saved != null) {
            saved
        } else {
            val default = "USD"
            saveDefaultCurrency(default)
            default
        }
    }


    fun saveCurrencyCache(json: String) {
        prefs.edit().putString("cached_currency_json", json).apply()
        prefs.edit().putLong("cached_currency_date", System.currentTimeMillis()).apply()
    }

    fun getCurrencyCache(): String? = prefs.getString("cached_currency_json", null)

    fun isCurrencyCacheToday(): Boolean {
        val savedTime = prefs.getLong("cached_currency_date", 0L)
        val savedDate = LocalDate.ofEpochDay(savedTime / (1000 * 60 * 60 * 24))
        val today = LocalDate.now()
        return savedDate.isEqual(today)
    }


    fun saveAppIconStyle(style: AppIconStyle) {
        prefs.edit().putString("app_icon_style", style.name).apply()
    }

    fun getAppIconStyle(): AppIconStyle {
        val name = prefs.getString("app_icon_style", AppIconStyle.CLASSIC.name)
        return AppIconStyle.valueOf(name!!)
    }

    fun saveAppTheme(theme: AppTheme) {
        prefs.edit().putString("app_theme", theme.name).apply()
    }

    fun getAppTheme(): AppTheme {
        val name = prefs.getString("app_theme", AppTheme.SYSTEM.name)
        return AppTheme.fromName(name)
    }


    fun saveNotificationTime(hours: Int, minutes: Int) {
        prefs.edit()
            .putInt("notification_hour", hours)
            .putInt("notification_minute", minutes)
            .apply()
    }

    fun getNotificationTime(): Pair<Int, Int> {
        val hour = prefs.getInt("notification_hour", 9)
        val minute = prefs.getInt("notification_minute", 0)
        return hour to minute
    }

    fun saveNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }

    fun areNotificationsEnabled(): Boolean {
        return prefs.getBoolean("notifications_enabled", false)
    }


}
