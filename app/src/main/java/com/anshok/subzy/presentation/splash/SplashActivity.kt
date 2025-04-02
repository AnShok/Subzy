package com.anshok.subzy.presentation.splash

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.anshok.subzy.databinding.ActivitySplashBinding
import com.anshok.subzy.presentation.root.RootActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(com.anshok.subzy.R.style.Theme_Subzy)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAppIcon()
        startIconAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, RootActivity::class.java))
            finish()
        }, 2000)
    }

    private fun setupAppIcon() {
        val packageManager = packageManager
        val currentActivity = ComponentName(this, this::class.java)
        val aliases = listOf(
            "ClassicAlias",
            "NeonRedAlias",
            "NeonBlueAlias",
            "GlitchAlias",
            "LightAlias"
        )

        val iconRes = when {
            isAliasEnabled(
                packageManager,
                "ClassicAlias"
            ) -> com.anshok.subzy.R.mipmap.ic_launcher_classic

            isAliasEnabled(
                packageManager,
                "NeonRedAlias"
            ) -> com.anshok.subzy.R.mipmap.ic_launcher_neon_red

            isAliasEnabled(
                packageManager,
                "NeonBlueAlias"
            ) -> com.anshok.subzy.R.mipmap.ic_launcher_neon_blue

            isAliasEnabled(
                packageManager,
                "GlitchAlias"
            ) -> com.anshok.subzy.R.mipmap.ic_launcher_glitch

            isAliasEnabled(
                packageManager,
                "LightAlias"
            ) -> com.anshok.subzy.R.mipmap.ic_launcher_light

            else -> com.anshok.subzy.R.mipmap.ic_launcher_classic
        }

        binding.splashAppIcon.setImageResource(iconRes)
    }

    private fun isAliasEnabled(pm: PackageManager, aliasName: String): Boolean {
        val fullName = "${packageName}.$aliasName"
        return pm.getComponentEnabledSetting(ComponentName(packageName, fullName)) ==
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }

    private fun startIconAnimation() {
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            fillAfter = true
        }

        val scaleUp = ScaleAnimation(
            0.85f, 1f,
            0.85f, 1f,
            AnimationSet.RELATIVE_TO_SELF, 0.5f,
            AnimationSet.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1000
            fillAfter = true
        }

        val animationSet = AnimationSet(true).apply {
            addAnimation(fadeIn)
            addAnimation(scaleUp)
        }

        binding.splashAppIcon.startAnimation(animationSet)
    }
}
