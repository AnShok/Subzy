package com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.databinding.BottomSheetNotificationReminderBinding
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NotificationReminderBottomSheet(private val onOptionSelected: (String) -> Unit) :
    BottomSheetDialogFragment() {

    private val binding: BottomSheetNotificationReminderBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обработка выбора уведомления
        binding.optionOneDay.safeDelayedClick {
            onOptionSelected("за 1 день")
            dismiss()
        }

        binding.optionThreeDays.safeDelayedClick {
            onOptionSelected("за 3 дня")
            dismiss()
        }

        binding.optionOneWeek.safeDelayedClick {
            onOptionSelected("за 1 неделю")
            dismiss()
        }

        binding.optionNoReminder.safeDelayedClick {
            onOptionSelected("без уведомлений")
            dismiss()
        }
    }
}
