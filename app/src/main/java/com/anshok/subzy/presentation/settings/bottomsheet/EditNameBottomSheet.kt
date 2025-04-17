package com.anshok.subzy.presentation.settings.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.databinding.BottomSheetEditNameBinding
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditNameBottomSheet(
    private val currentName: String,
    private val onNameSaved: (String) -> Unit
) : BottomSheetDialogFragment() {

    private val binding: BottomSheetEditNameBinding by viewBinding(CreateMethod.INFLATE)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameEditText.setText(currentName)

        binding.saveButton.safeDelayedClick {
            val newName = binding.nameEditText.text.toString().trim()
            if (newName.isNotEmpty()) {
                onNameSaved(newName)
                dismiss()
            }
        }

        binding.cancelButton.safeDelayedClick {
            dismiss()
        }
    }
}
