package com.anshok.subzy.data.local

import android.content.Context
import com.anshok.subzy.domain.model.AppIconStyle
import com.anshok.subzy.domain.model.AppTheme

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
        return prefs.getString("default_currency_code", "USD") ?: "USD"
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


}
