package com.anshok.subzy.domain.settings.model

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.DrawableRes
import com.anshok.subzy.R

enum class AppIconStyle(
    val label: String,
    val component: String,
    @DrawableRes val iconRes: Int
) {
    CLASSIC("Classic", "com.anshok.subzy.ClassicAlias", R.mipmap.ic_launcher_classic),
    NEON_RED("Neon Red", "com.anshok.subzy.NeonRedAlias", R.mipmap.ic_launcher_neon_red),
    NEON_BLUE("Neon Blue", "com.anshok.subzy.NeonBlueAlias", R.mipmap.ic_launcher_neon_blue),
    GLITCH("Glitch", "com.anshok.subzy.GlitchAlias", R.mipmap.ic_launcher_glitch),
    LIGHT("Dark", "com.anshok.subzy.LightAlias", R.mipmap.ic_launcher_light);

    companion object {
        fun fromComponent(component: String): AppIconStyle {
            return values().find { it.component == component } ?: CLASSIC
        }

        fun getCurrentComponentName(context: Context): String? {
            val packageManager = context.packageManager
            val packageName = context.packageName

            return AppIconStyle.values()
                .map { it.component }
                .firstOrNull { componentName ->
                    val state = packageManager.getComponentEnabledSetting(
                        ComponentName(
                            packageName,
                            componentName
                        )
                    )
                    state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                }
        }
    }
}
