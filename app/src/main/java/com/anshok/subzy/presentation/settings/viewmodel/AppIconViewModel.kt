package com.anshok.subzy.presentation.settings.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshok.subzy.data.local.UserPreferences
import com.anshok.subzy.domain.model.AppIconStyle
import com.anshok.subzy.domain.model.AppIconStyle.Companion.getCurrentComponentName
import com.anshok.subzy.util.switchAppIcon

class AppIconViewModel(
    private val preferences: UserPreferences,
    private val app: Application
) : ViewModel() {

    private val _selectedStyle = MutableLiveData<AppIconStyle>().apply {
        val actualComponent = getCurrentComponentName(app)
        value =
            actualComponent?.let { AppIconStyle.fromComponent(it) } ?: preferences.getAppIconStyle()
    }

    val selectedStyle: LiveData<AppIconStyle> = _selectedStyle

    fun selectStyle(style: AppIconStyle) {
        preferences.saveAppIconStyle(style)
        _selectedStyle.value = style
        switchAppIcon(app, style)
    }
}
