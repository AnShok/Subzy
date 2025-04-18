package com.anshok.subzy.domain.settings

interface SettingsRepository {
    fun saveProfileImagePath(path: String)
    fun getProfileImagePath(): String?

    fun saveUserName(name: String)
    fun getUserName(): String?
}
