package com.anshok.subzy.presentation.ui.bottomSheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.databinding.FragmentDescriptionBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DescriptionBottomSheet(private val onOptionSelected: (String?) -> Unit) :
    BottomSheetDialogFragment() {

    private val binding: FragmentDescriptionBottomSheetBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Следим за вводом текста в EditText
        binding.descriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        })

        // Обработка нажатия кнопки "ОК"
        binding.saveButton.setOnClickListener {
            val description = binding.descriptionEditText.text.toString()
            onOptionSelected(description)
            dismiss()
        }

        // Обработка нажатия кнопки "Отмена"
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            // Показываем клавиатуру автоматически при открытии
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    override fun onResume() {
        super.onResume()
        // Задержка для корректного отображения клавиатуры
        binding.descriptionEditText.post {
            binding.descriptionEditText.requestFocus()
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.descriptionEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}