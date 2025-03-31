package com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anshok.subzy.databinding.BottomSheetPaymentPeriodBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentPeriodBottomSheet(private val onPeriodSelected: (Int, String) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPaymentPeriodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetPaymentPeriodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Настройка NumberPicker для чисел
        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 30

        // Настройка NumberPicker для периодов
        val periods = arrayOf("дни", "недели", "месяцы", "годы")
        binding.periodPicker.minValue = 0
        binding.periodPicker.maxValue = periods.size - 1
        binding.periodPicker.displayedValues = periods

        // Обработка нажатия кнопки "Готово"
        binding.confirmButton.setOnClickListener {
            val selectedNumber = binding.numberPicker.value
            val selectedPeriod = periods[binding.periodPicker.value]
            onPeriodSelected(selectedNumber, selectedPeriod)
            dismiss()
        }
    }
}