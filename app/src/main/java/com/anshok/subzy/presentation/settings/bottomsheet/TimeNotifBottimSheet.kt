package com.anshok.subzy.presentation.settings.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anshok.subzy.databinding.BottomSheetTimeNotificationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TimeNotifBottomSheet(
    private val onTimeSaved: (Int, Int) -> Unit,
    private val initialTime: Pair<Int, Int>
) : BottomSheetDialogFragment() {


    private var _binding: BottomSheetTimeNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTimeNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hours.apply {
            minValue = 0
            maxValue = 23
            setFormatter { i -> String.format("%02d", i) }
        }

        binding.minutes.apply {
            minValue = 0
            maxValue = 59
            setFormatter { i -> String.format("%02d", i) }
        }


        binding.hours.value = initialTime.first
        binding.minutes.value = initialTime.second

        binding.confirmButton.setOnClickListener {
            onTimeSaved(binding.hours.value, binding.minutes.value)
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
