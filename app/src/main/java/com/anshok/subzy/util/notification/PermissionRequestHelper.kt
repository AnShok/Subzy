package com.anshok.subzy.util.notification

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.anshok.subzy.R

object PermissionRequestHelper {

//    fun isPermissionGranted(context: Context): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        } else {
//            true
//        }
//    }

    fun openNotificationSettings(context: Context) {
        val intent = Intent().apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent)
    }

    fun openAlarmPermissionSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(intent)
        } else {
            // Для старых версий Android — ничего делать не нужно
        }
    }

    /**
     * Предлагает открыть настройки, если разрешение не выдано
     * Возвращает true, если разрешение есть
     */
    fun requestOrRedirectIfNeeded(context: Context, onGranted: () -> Unit, onDenied: () -> Unit) {
        if (hasAllRequiredPermissions(context)) {
            onGranted()
        } else {
            openNotificationSettings(context)
            onDenied()
        }
    }

    fun hasAllRequiredPermissions(context: Context): Boolean {
        val hasNotif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true

        val hasExactAlarm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            alarmManager?.canScheduleExactAlarms() == true
        } else true

        return hasNotif && hasExactAlarm
    }

    fun updatePermissionIcon(context: Context, imageView: ImageView, type: PermissionType) {
        val isGranted = when (type) {
            PermissionType.NOTIFICATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                } else true
            }
            PermissionType.ALARM -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                    alarmManager?.canScheduleExactAlarms() == true
                } else true
            }
        }

        imageView.setImageResource(
            if (isGranted) R.drawable.ic_check_big else R.drawable.ic_external_link
        )
    }

    enum class PermissionType {
        NOTIFICATION,
        ALARM
    }


    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }


}
