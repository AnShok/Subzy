package com.anshok.subzy.presentation.subDetails.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.anshok.subzy.databinding.DeleteConfirmationDialogBinding
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteConfirmationDialog(
    private val onConfirm: () -> Unit
) : DialogFragment() {

    private var _binding: DeleteConfirmationDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DeleteConfirmationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelButton.safeDelayedClick { dismiss() }

        binding.confirmDeleteButton.safeDelayedClick {
            onConfirm()
            dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
            setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
