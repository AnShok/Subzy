package com.anshok.subzy.domain.settings

import android.content.Context
import android.net.Uri
import com.anshok.subzy.R
import java.io.File

class SettingsInteractorImpl(
    private val repository: SettingsRepository,
    private val context: Context
) : SettingsInteractor {

    override fun saveProfileImage(uri: Uri) {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return
        val file = File(context.filesDir, "profile_image.jpg")
        file.outputStream().use { output -> inputStream.copyTo(output) }
        repository.saveProfileImagePath(file.absolutePath)
    }

    override fun getProfileImageUri(): Uri? {
        val path = repository.getProfileImagePath() ?: return null
        val file = File(path)
        return if (file.exists()) Uri.fromFile(file) else null
    }

    override fun saveUserName(name: String) {
        repository.saveUserName(name)
    }

    override fun getUserName(): String {
        return repository.getUserName() ?: "Unnamed"
    }

    override fun getShareText(): String {
        return context.getString(R.string.share_app_text)
    }
}

