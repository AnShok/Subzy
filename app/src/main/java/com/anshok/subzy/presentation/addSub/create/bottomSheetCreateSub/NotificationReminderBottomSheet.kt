package com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.databinding.BottomSheetNotificationReminderBinding
import com.anshok.subzy.presentation.common.PermissionDialogFragment
import com.anshok.subzy.util.notification.PermissionRequestHelper
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//class NotificationReminderBottomSheet(private val onOptionSelected: (String) -> Unit) :
//    BottomSheetDialogFragment() {
//
//    private val binding: BottomSheetNotificationReminderBinding by viewBinding(CreateMethod.INFLATE)
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View {
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val context = requireContext()
//
//        fun selectOption(option: String) {
//            onOptionSelected(option)
//            dismiss()
//        }
//
//        fun showPermissionDialog() {
//            PermissionDialogFragment {
//                if (PermissionRequestHelper.hasAllRequiredPermissions(context)) {
//                    // Пользователь выдал разрешение — не меняем selectedOption в этот момент
//                }
//            }.show(parentFragmentManager, "PermissionDialog")
//        }
//
//        fun checkPermissionOrShowDialog(option: String) {
//            if (PermissionRequestHelper.hasAllRequiredPermissions(context)) {
//                selectOption(option)
//            } else {
//                showPermissionDialog()
//            }
//        }
//
//        binding.optionOneDay.safeDelayedClick {
//            checkPermissionOrShowDialog("за 1 день")
//        }
//
//        binding.optionThreeDays.safeDelayedClick {
//            checkPermissionOrShowDialog("за 3 дня")
//        }
//
//        binding.optionOneWeek.safeDelayedClick {
//            checkPermissionOrShowDialog("за 1 неделю")
//        }
//
//        binding.optionNoReminder.safeDelayedClick {
//            selectOption("без уведомлений")
//        }
//    }
//}
