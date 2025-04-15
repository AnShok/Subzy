package com.anshok.subzy.presentation.settings.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.BottomSheetHelpBinding
import com.anshok.subzy.presentation.settings.viewmodel.HelpViewModel
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelpBottomSheet : BottomSheetDialogFragment() {

    private val binding: BottomSheetHelpBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: HelpViewModel by viewModel()

    override fun getTheme() = R.style.BottomSheetDialogResizeStyle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.openIntent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                startActivity(Intent.createChooser(it, getString(R.string.choose_email)))
                dismiss()
            }
        }

        binding.emailContainer.safeDelayedClick {
            viewModel.onEmailClick()
        }
        binding.telegramContainer.safeDelayedClick {
            viewModel.onTelegramClick()
        }
    }
}


