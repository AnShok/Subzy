package com.anshok.subzy.presentation.settings.bottomSheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.BottomSheetCurrencySelectorBinding
import com.anshok.subzy.databinding.BottomSheetFeedbackBinding
import com.anshok.subzy.presentation.settings.viewmodel.RateViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedbackBottomSheet : BottomSheetDialogFragment() {

    private val binding: BottomSheetFeedbackBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: RateViewModel by viewModel()

    override fun getTheme(): Int = R.style.BottomSheetDialogResizeStyle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendEmailButton.setOnClickListener {
            viewModel.onSendEmailClicked()
            dismiss()
        }

        binding.declineButton.setOnClickListener {
            dismiss()
        }

        viewModel.openEmail.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                startActivity(Intent.createChooser(it, getString(R.string.choose_email)))
            }
        }
    }
}
