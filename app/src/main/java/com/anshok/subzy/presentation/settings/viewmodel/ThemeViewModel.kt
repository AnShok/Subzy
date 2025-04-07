package com.anshok.subzy.presentation.settings.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.domain.settings.model.AppTheme

class ThemeViewModel(
    private val preferences: UserPreferences
) : ViewModel() {

    private val _selectedTheme = MutableLiveData(preferences.getAppTheme())
    val selectedTheme: LiveData<AppTheme> = _selectedTheme

    fun setTheme(theme: AppTheme) {
        preferences.saveAppTheme(theme)
        _selectedTheme.value = theme

        when (theme) {
            AppTheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AppTheme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
