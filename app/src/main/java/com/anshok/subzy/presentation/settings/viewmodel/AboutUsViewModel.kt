package com.anshok.subzy.presentation.settings.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anshok.subzy.util.getCurrentAppIconStyle

class AboutUsViewModel(application: Application) : AndroidViewModel(application) {

    private val _version = MutableLiveData<String>()
    val version: LiveData<String> = _version

    private val _appIconRes = MutableLiveData<Int>()
    val appIconRes: LiveData<Int> = _appIconRes

    init {
        loadAppInfo()
    }

    private fun loadAppInfo() {
        val context = getApplication<Application>()
        val packageManager = context.packageManager
        val packageName = context.packageName

        val versionName = try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "1.0"
        }
        _version.value = "v$versionName"

        val iconStyle = getCurrentAppIconStyle(context)
        _appIconRes.value = iconStyle.iconRes
    }
}
