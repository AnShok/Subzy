package com.anshok.subzy.presentation.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentAboutUsBinding
import com.anshok.subzy.presentation.settings.bottomSheet.EasterEggBottomSheet
import com.anshok.subzy.presentation.settings.viewmodel.AboutUsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutUsFragment : Fragment() {

    private val binding: FragmentAboutUsBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: AboutUsViewModel by viewModel()

    private var tapCount = 0
    private var lastTapTime = 0L
    private val tapThreshold = 500L
    private var easterEggShown = false
    private var originalIconRes: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appIconImage.apply {
            clipToOutline = true
            outlineProvider = ViewOutlineProvider.BACKGROUND
        }

        observeViewModel()
        setupUI()
        setupEasterEggGesture()
    }

    private fun observeViewModel() {
        viewModel.version.observe(viewLifecycleOwner) {
            binding.appVersionText.text = it
        }

        viewModel.appIconRes.observe(viewLifecycleOwner) {
            originalIconRes = it
            binding.appIconImage.setImageResource(it)
        }
    }

    private fun setupUI() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.privacyPolicy.setOnClickListener {
            openWeb("https://yourdomain.com/privacy")
        }
        binding.termsOfUse.setOnClickListener {
            openWeb("https://yourdomain.com/terms")
        }
        binding.logoDevLink.setOnClickListener {
            openWeb("https://logo.dev")
        }
    }

    private fun setupEasterEggGesture() {
        val gestureDetector = GestureDetector(requireContext(), EasterEggGestureListener())

        @Suppress("ClickableViewAccessibility")
        binding.appIconImage.setOnTouchListener { view, event ->
            gestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_DOWN) {
                val currentTime = System.currentTimeMillis()
                tapCount = if (currentTime - lastTapTime < tapThreshold) tapCount + 1 else 1
                lastTapTime = currentTime
            }
            view.performClick()
            true
        }
    }

    private inner class EasterEggGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, vx: Float, vy: Float): Boolean {
            if (!easterEggShown && tapCount >= 5 && e1 != null && e2.y < e1.y - 100) {
                triggerEasterEgg()
                tapCount = 0
                easterEggShown = true
            }
            return true
        }
    }

    private fun triggerEasterEgg() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))

        binding.appIconImage.animate().rotationBy(2160f).setDuration(2000).start()

        Handler(Looper.getMainLooper()).postDelayed({
            EasterEggBottomSheet(onMagicRevealed = { animateAndSetMagicIcon() })
                .show(parentFragmentManager, "EasterEggBottomSheet")
        }, 1000)
    }

    private fun animateAndSetMagicIcon() {
        // Стартуем вращение
        binding.appIconImage.animate()
            .rotationBy(5760f)
            .setDuration(4000)
            .start()

        // Меняем картинку ПОСЛЕ 2 секунд (в середине вращения)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.appIconImage.setImageResource(R.drawable.photo)
        }, 2000)
    }


    private fun openWeb(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        originalIconRes?.let { binding.appIconImage.setImageResource(it) }
        easterEggShown = false
        tapCount = 0
    }
}
