package com.anshok.subzy.presentation.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshok.subzy.data.local.preferences.UserPreferences

class NotificationSettingsViewModel(
    private val prefs: UserPreferences
) : ViewModel() {

    private val _notificationTime = MutableLiveData<Pair<Int, Int>>()
    val notificationTime: LiveData<Pair<Int, Int>> get() = _notificationTime

    private val _notificationsEnabled = MutableLiveData<Boolean>()
    val notificationsEnabled: LiveData<Boolean> get() = _notificationsEnabled

    fun loadNotificationSettings() {
        _notificationTime.value = prefs.getNotificationTime()
        _notificationsEnabled.value = prefs.areNotificationsEnabled()
    }

    fun setNotificationTime(hour: Int, minute: Int) {
        prefs.saveNotificationTime(hour, minute)
        _notificationTime.value = hour to minute
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.saveNotificationsEnabled(enabled)
        _notificationsEnabled.value = enabled
    }
}
