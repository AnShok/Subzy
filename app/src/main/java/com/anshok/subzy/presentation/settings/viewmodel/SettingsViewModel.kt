package com.anshok.subzy.presentation.settings.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.R
import com.anshok.subzy.domain.api.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val appContext: Context
) : ViewModel() {

    private val _profileImage = MutableLiveData<Uri?>()
    val profileImage: LiveData<Uri?> = _profileImage

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun loadImage() {
        val path = repository.getProfileImagePath() ?: return
        val file = File(path)
        if (file.exists()) {
            _profileImage.value = Uri.fromFile(file)
        }
    }

    fun saveImage(uri: Uri) {
        val inputStream = appContext.contentResolver.openInputStream(uri) ?: return
        val file = File(appContext.filesDir, "profile_image.jpg")
        file.outputStream().use { output -> inputStream.copyTo(output) }
        repository.saveProfileImagePath(file.absolutePath)

        viewModelScope.launch {
            _profileImage.value = null
            delay(10)
            _profileImage.value = Uri.fromFile(file)
        }
    }

    fun loadUserName() {
        _userName.value = repository.getUserName() ?: "Unnamed"
    }

    fun saveUserName(name: String) {
        repository.saveUserName(name)
        _userName.value = name
    }

    fun getShareText(): String {
        return appContext.getString(R.string.share_app_text)
    }


}
