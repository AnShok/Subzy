package com.anshok.subzy.domain.settings

import android.net.Uri

interface SettingsInteractor {
    fun saveProfileImage(uri: Uri)
    fun getProfileImageUri(): Uri?
    fun saveUserName(name: String)
    fun getUserName(): String
    fun getShareText(): String
}

