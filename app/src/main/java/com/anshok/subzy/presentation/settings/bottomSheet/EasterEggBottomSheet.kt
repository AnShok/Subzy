package com.anshok.subzy.presentation.settings.bottomSheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.anshok.subzy.R
import com.anshok.subzy.databinding.BottomSheetEasterEggBinding
import com.anshok.subzy.presentation.settings.viewmodel.EasterEggViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel


class EasterEggBottomSheet(
    private val onMagicRevealed: (() -> Unit)? = null
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEasterEggBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EasterEggViewModel by viewModel()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEasterEggBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(500)
            runScenario()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
                behavior.isDraggable = false
                behavior.isFitToContents = true
            }

            // Убираем затемнение фона при показе
            window?.setDimAmount(0f)

            // Убираем фоновый drawable у самого окна
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


    private fun runScenario(startIndex: Int = 0) {
        viewModel.messages.drop(startIndex).forEachIndexed { index, text ->
            handler.postDelayed({
                val messageView = createMessageView(text)
                applySlideUpFadeIn(messageView)
                binding.messageContainer.addView(messageView)

                if (text.contains("⬆\uFE0F")) onMagicRevealed?.invoke()
                if (index == viewModel.messages.drop(startIndex).lastIndex) {
                    handler.postDelayed({ dismissAllowingStateLoss() }, 5000)
                }
            }, index * 4000L)
        }
    }

    private fun createMessageView(text: String): TextView {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_easter_message, binding.messageContainer, false)
        return (view as TextView).apply {
            this.text = text
        }
    }


    private fun applySlideUpFadeIn(view: View) {
        val animSet = AnimationSet(true).apply {
            addAnimation(TranslateAnimation(0f, 0f, 100f, 0f).apply { duration = 400 })
            addAnimation(AlphaAnimation(0f, 1f).apply { duration = 400 })
        }
        view.startAnimation(animSet)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }
}
