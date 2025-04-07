package com.anshok.subzy.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentActivity
import com.anshok.subzy.domain.settings.model.AppIconStyle
import com.anshok.subzy.presentation.settings.RestartDialogFragment

fun switchAppIcon(context: Context, style: AppIconStyle) {
    val pm = context.packageManager
    val packageName = context.packageName
    val newComponent = ComponentName(packageName, style.component)

    val isAlreadyEnabled = pm.getComponentEnabledSetting(newComponent) ==
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED

    if (isAlreadyEnabled) return

    if (context is FragmentActivity) {
        RestartDialogFragment {
            performIconSwitch(context, style)
        }.show(context.supportFragmentManager, "RestartDialog")
    } else {
        performIconSwitch(context, style)
    }
}

private fun performIconSwitch(context: Context, style: AppIconStyle) {
    val pm = context.packageManager
    val packageName = context.packageName
    val newComponent = ComponentName(packageName, style.component)

    // Отключаем все alias'ы
    AppIconStyle.values().forEach {
        val component = ComponentName(packageName, it.component)
        pm.setComponentEnabledSetting(
            component,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    // Включаем нужный alias с задержкой
    Handler(Looper.getMainLooper()).postDelayed({
        pm.setComponentEnabledSetting(
            newComponent,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        // Завершаем процесс ещё через 500мс
        Handler(Looper.getMainLooper()).postDelayed({
            android.os.Process.killProcess(android.os.Process.myPid())
        }, 500)

    }, 300)
}

fun getCurrentAppIconStyle(context: Context): AppIconStyle {
    val pm = context.packageManager
    val packageName = context.packageName

    return AppIconStyle.values().firstOrNull {
        val componentName = ComponentName(packageName, it.component)
        pm.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    } ?: AppIconStyle.CLASSIC
}

