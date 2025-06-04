package com.anshok.subzy.presentation.common

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.anshok.subzy.R
import com.anshok.subzy.databinding.PermissionConfirmationDialogBinding
import com.anshok.subzy.util.notification.PermissionRequestHelper
import com.google.android.material.snackbar.Snackbar

//class PermissionDialogFragment(
//    private val onClose: () -> Unit
//) : DialogFragment() {
//
//    private lateinit var binding: PermissionConfirmationDialogBinding
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        binding = PermissionConfirmationDialogBinding.inflate(LayoutInflater.from(context))
//
//        setupUI()
//
//        return AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
//            .setView(binding.root)
//            .create()
//    }
//
//    private fun setupUI() {
//        updateIcons()
//
//        binding.allowNotificationsContainer.setOnClickListener {
//            PermissionRequestHelper.openNotificationSettings(requireContext())
//        }
//
//        binding.allowAlarmNotificationsContainer.setOnClickListener {
//            PermissionRequestHelper.openAlarmPermissionSettings(requireContext())
//        }
//
//        binding.cancelButton.setOnClickListener {
//            dismiss()
//        }
//    }
//
//    private fun updateIcons() {
//        PermissionRequestHelper.updatePermissionIcon(
//            requireContext(),
//            binding.allowNotificationsLink, PermissionRequestHelper.PermissionType.NOTIFICATION
//        )
//        PermissionRequestHelper.updatePermissionIcon(
//            requireContext(),
//            binding.allowAlarmNotificationsLink, PermissionRequestHelper.PermissionType.ALARM
//        )
//    }
//
//    private fun hasAllPermissions(): Boolean {
//        return PermissionRequestHelper.hasAllRequiredPermissions(requireContext())
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val context = requireContext()
//
//        if (PermissionRequestHelper.hasAllRequiredPermissions(context)) {
//            updateIcons()
//            binding.allowAlarmNotificationsLink.setImageResource(R.drawable.ic_check_big)
//            binding.allowNotificationsLink.setImageResource(R.drawable.ic_check_big)
//            binding.allowNotificationsLink.postDelayed({
//                dismiss() // вызовет onClose()
//            }, 2000)
//        } else {
//            updateIcons()
//        }
//    }
//
//
//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//        val context = requireContext()
//        val hasAllPermissions = PermissionRequestHelper.hasAllRequiredPermissions(context)
//
//        if (hasAllPermissions) {
//            onClose()
//        } else {
//            Snackbar.make(
//                requireActivity().findViewById(android.R.id.content),
//                R.string.notifications_not_enabled,
//                Snackbar.LENGTH_SHORT
//            )
//                .setBackgroundTint(context.getColor(R.color.Accent_P_100))
//                .setTextColor(context.getColor(R.color.white))
//                .show()
//        }
//    }
//
//}