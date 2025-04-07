package com.anshok.subzy.data.repository.impl

import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.domain.settings.SettingsRepository

class SettingsRepositoryImpl(private val userPreferences: UserPreferences) : SettingsRepository {
    override fun saveProfileImagePath(path: String) = userPreferences.saveProfileImagePath(path)
    override fun getProfileImagePath(): String? = userPreferences.getProfileImagePath()

    override fun saveUserName(name: String) = userPreferences.saveUserName(name)
    override fun getUserName(): String? = userPreferences.getUserName()
}
