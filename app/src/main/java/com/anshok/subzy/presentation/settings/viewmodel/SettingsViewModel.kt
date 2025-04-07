package com.anshok.subzy.presentation.settings.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.domain.settings.SettingsInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class SettingsViewModel(
    private val interactor: SettingsInteractor
) : ViewModel() {

    private val _profileImage = MutableLiveData<Uri?>()
    val profileImage: LiveData<Uri?> = _profileImage

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun loadImage() {
        _profileImage.value = interactor.getProfileImageUri()
    }

    fun saveImage(uri: Uri) {
        interactor.saveProfileImage(uri)

        viewModelScope.launch {
            _profileImage.value = null
            delay(10)
            _profileImage.value = interactor.getProfileImageUri()
        }
    }

    fun loadUserName() {
        _userName.value = interactor.getUserName() ?: "Unnamed"
    }

    fun saveUserName(name: String) {
        interactor.saveUserName(name)
        _userName.value = name
    }

    fun getShareText(): String = interactor.getShareText()
}
