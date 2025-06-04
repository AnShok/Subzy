package com.anshok.subzy.presentation.addSub.create

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentAddSubCreateBinding
import com.anshok.subzy.domain.paymentPeriod.model.PaymentPeriodType
//import com.anshok.subzy.domain.reminder.model.ReminderType
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub.DescriptionBottomSheet
//import com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub.NotificationReminderBottomSheet
import com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub.PaymentPeriodBottomSheet
import com.anshok.subzy.presentation.addSub.create.dialog.CancelConfirmationDialog
import com.anshok.subzy.presentation.addSub.create.state.SaveResult
import com.anshok.subzy.presentation.common.CurrencyPickerBottomSheet
import com.anshok.subzy.presentation.common.CustomErrorDialogFragment
import com.anshok.subzy.util.VibrationUtils
import com.anshok.subzy.util.adapter.bindLogo
import com.anshok.subzy.util.adapter.toLogo
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.anshok.subzy.util.extension.toReminderType
import com.anshok.subzy.util.extension.toLabel
import kotlinx.coroutines.launch


class AddSubCreateFragment : Fragment() {

    private val binding: FragmentAddSubCreateBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: AddSubCreateViewModel by viewModel()

    private var selectedPeriodNumber: Int = 1
    private var selectedPeriodType: PaymentPeriodType = PaymentPeriodType.MONTHLY
    private var originalSubscription: Subscription? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                viewModel.selectedImageFromGallery = it.toString()
                bindLogo(it.toString().toLogo(requireContext()), binding.itemLogo)
                checkChanges()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialAnimations()
        observeCurrency()
        setupClickListeners()
        setupPassedArgumentsOrLoadFromId()
        setupPriceValidation()
        setupValidation()

    }

    private fun setupInitialAnimations() {
        binding.root.alpha = 0f
        binding.root.animate().alpha(1f).setDuration(250).start()
    }

    private fun observeCurrency() {
        viewModel.currencyCode.observe(viewLifecycleOwner) { code ->
            binding.currencyButton.text = code
        }
    }

    private fun setupClickListeners() {
        binding.backButton.safeDelayedClick {
            findNavController().navigateUp()
        }

        binding.addButton.safeDelayedClick {
            VibrationUtils.vibrateLight(requireContext())
            onAddClicked()
        }

        binding.saveButton.safeDelayedClick {
            VibrationUtils.vibrateLight(requireContext())
            onSaveClicked()
        }

        binding.cancelButton.safeDelayedClick {
            CancelConfirmationDialog {
                findNavController().popBackStack()
            }.show(parentFragmentManager, "CancelConfirmationDialog")
        }

        binding.currencyButton.setOnClickListener {
            viewModel.loadCurrencies { currencies ->
                CurrencyPickerBottomSheet(
                    currencies = currencies,
                    currentCode = viewModel.currencyCode.value.orEmpty(),
                    onCurrencySelected = { viewModel.setCurrency(it) }
                ).show(parentFragmentManager, "CurrencyPicker")
            }
        }

        binding.itemLogo.safeDelayedClick {
            galleryLauncher.launch("image/*")
        }

        binding.descriptionContainer.setOnClickListener { openDescriptionBottomSheet() }
        binding.paymentPeriodContainer.setOnClickListener { openPaymentPeriodBottomSheet() }
        binding.firstPaymentContainer.setOnClickListener { showDatePicker() }
//        binding.reminderContainer.setOnClickListener { openReminderBottomSheet() }
//        binding.commentContainer.setOnClickListener { openCommentBottomSheet() }
//
//        binding.categoryContainer.setOnClickListener {
//            CategoryBottomSheetFragment { category ->
//                binding.categoryValue.text = category
//            }.show(parentFragmentManager, "CategoryBottomSheet")
//        }

        setCurrentDate()
    }

    private fun setupPassedArguments() {
        arguments?.let { bundle ->
            bundle.getString("logoName")?.let {
                binding.subscriptionNameEditTxt.setText(it)
            }

            bundle.getString("logoUrl")?.let {
                viewModel.selectedLogoUrl = it
                bindLogo(it.toLogo(requireContext()), binding.itemLogo)

            }

            val logoResId = bundle.getInt("logoResId")
            if (logoResId != 0) {
                val resName = requireContext().resources.getResourceEntryName(logoResId)
                viewModel.selectedLogoResName = resName
                bindLogo("res://$resName".toLogo(requireContext()), binding.itemLogo)
            }
        }
    }

    private fun setupPassedArgumentsOrLoadFromId() {
        val subscriptionIdRaw = arguments?.getString("subscriptionId")
        val subscriptionId = subscriptionIdRaw?.toLongOrNull()

        val isEdit = arguments?.getBoolean("isEdit") ?: false

        if (subscriptionId != null && isEdit) {
            viewModel.loadSubscriptionForEdit(subscriptionId) { subscription ->
                originalSubscription = subscription
                binding.subscriptionNameEditTxt.setText(subscription.name)
                binding.subscriptionPriceEditTxt.setText(subscription.price.toString())

                val desc = subscription.description
                binding.descriptionValue.text = if (desc.isNullOrBlank()) "Not specified" else desc

                binding.firstPaymentValue.text = viewModel.formatDate(subscription.firstPaymentDate)
                binding.reminderValue.text = subscription.reminderType.toLabel()
                binding.paymentPeriodValue.text =
                    "${subscription.paymentPeriod} ${subscription.paymentPeriodType.name.lowercase()}"
                selectedPeriodNumber = subscription.paymentPeriod
                selectedPeriodType = subscription.paymentPeriodType

                // логотип
                bindLogo(subscription.logoUrl.toLogo(requireContext()), binding.itemLogo)

                // валюта 👇
                viewModel.setCurrency(subscription.currencyCode)

                // заголовок
                binding.title.text = "Edit Subscription"

                // скрыть кнопку Add
                binding.addButton.visibility = View.GONE
            }
            // Включаем слежку за изменениями
            enableEditModeWatcher()
        } else {
            setupPassedArguments() // Логика из поиска
        }
    }


    private fun setupPriceValidation() {
        binding.subscriptionPriceEditTxt.doAfterTextChanged { text ->
            val clean = text.toString().replace(",", ".")
            val parts = clean.split(".")
            if (parts.size > 1 && parts[1].length > 2) {
                val fixed = "${parts[0]}.${parts[1].take(2)}"
                binding.subscriptionPriceEditTxt.setText(fixed)
                binding.subscriptionPriceEditTxt.setSelection(fixed.length)
            }
        }
    }

    private fun setupValidation() {
        binding.subscriptionNameEditTxt.doAfterTextChanged { validateFields() }
        binding.subscriptionPriceEditTxt.doAfterTextChanged { validateFields() }

        binding.firstPaymentValue.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            validateFields()
        }

        validateFields()
    }

    private fun onAddClicked() {
        val name = binding.subscriptionNameEditTxt.text?.toString()?.trim().orEmpty()
        val price =
            binding.subscriptionPriceEditTxt.text?.toString()?.replace(",", ".")?.toDoubleOrNull()
                ?: 0.0
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateMillis = dateFormat.parse(binding.firstPaymentValue.text.toString())?.time
            ?: System.currentTimeMillis()
        val reminderType = binding.reminderValue.text.toString().toReminderType()

        viewModel.addSubscription(
            context = requireContext(),
            name = name,
            price = price,
            description = binding.descriptionValue.text.toString()
                .takeIf { it.isNotBlank() && it != "Not specified" },
            paymentPeriod = selectedPeriodNumber,
            paymentPeriodType = selectedPeriodType,
            firstPaymentDate = dateMillis,
            categoryId = 0L,
            paymentMethodId = 0L,
            reminderType = reminderType

            //comment = binding.commentValue.text.toString().takeIf { it != "Not specified" }
        ) { result ->
            when (result) {
                SaveResult.Success -> {
                    findNavController().apply {
                        popBackStack(R.id.homeFragment, false)
                    }
                }

                SaveResult.Duplicate -> {
                    showErrorDialog(getString(R.string.subscription_exists))
                }

                SaveResult.InvalidInput -> {
                    showErrorDialog(getString(R.string.subscription_invalid_input))
                }
            }
        }
    }


    private fun setCurrentDate() {
        val formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        binding.firstPaymentValue.text = formattedDate
    }

    private fun showDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select first payment date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val date = Date(selection)
            val formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
            binding.firstPaymentValue.text = formattedDate
            validateFields()
            checkChanges()
        }
    }


    private fun openDescriptionBottomSheet() {
        DescriptionBottomSheet { desc ->
            if (!desc.isNullOrBlank()) binding.descriptionValue.text = desc
        }.show(parentFragmentManager, "DescriptionBottomSheet")
    }

    private fun openPaymentPeriodBottomSheet() {
        PaymentPeriodBottomSheet { number, periodString ->
            val type = when (periodString) {
                "дни" -> PaymentPeriodType.DAILY
                "недели" -> PaymentPeriodType.WEEKLY
                "месяцы" -> PaymentPeriodType.MONTHLY
                "годы" -> PaymentPeriodType.YEARLY
                else -> PaymentPeriodType.MONTHLY
            }
            selectedPeriodNumber = number
            selectedPeriodType = type

            val correctForm = when (type) {
                PaymentPeriodType.DAILY -> resources.getQuantityString(
                    R.plurals.days,
                    number,
                    number
                )

                PaymentPeriodType.WEEKLY -> resources.getQuantityString(
                    R.plurals.weeks,
                    number,
                    number
                )

                PaymentPeriodType.MONTHLY -> resources.getQuantityString(
                    R.plurals.months,
                    number,
                    number
                )

                PaymentPeriodType.YEARLY -> resources.getQuantityString(
                    R.plurals.years,
                    number,
                    number
                )
            }

            binding.paymentPeriodValue.text = correctForm
        }.show(parentFragmentManager, "PaymentPeriodBottomSheet")
    }

//    private fun openReminderBottomSheet() {
//        NotificationReminderBottomSheet { selectedOption ->
//            binding.reminderValue.text = selectedOption
//        }.show(parentFragmentManager, "NotificationReminderBottomSheet")
//    }

//    private fun openCommentBottomSheet() {
//        CommentBottomSheet { comment ->
//            if (!comment.isNullOrBlank()) binding.commentValue.text = comment
//        }.show(parentFragmentManager, "CommentBottomSheet")
//    }

    private fun showErrorDialog(message: String) {
        CustomErrorDialogFragment(
            message = message
        ).show(parentFragmentManager, "CustomErrorDialog")
    }


    private fun validateFields() {
        val isNameValid = binding.subscriptionNameEditTxt.text?.isNotBlank() == true
        val isPriceValid = binding.subscriptionPriceEditTxt.text?.toString()?.toDoubleOrNull()
            ?.let { it > 0 } == true
        val isDateValid = binding.firstPaymentValue.text?.isNotBlank() == true

        val enabled = isNameValid && isPriceValid && isDateValid

        binding.addButton.isEnabled = enabled
        binding.addButton.backgroundTintList = ContextCompat.getColorStateList(
            requireContext(),
            if (enabled) R.color.Accent_P_100 else R.color.Gray_30
        )
    }


    private fun onSaveClicked() {
        val subId = arguments?.getString("subscriptionId")?.toLongOrNull() ?: return
        viewModel.loadSubscriptionForEdit(subId) { original ->
            val newFirstPaymentDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                .parse(binding.firstPaymentValue.text.toString())?.time
                ?: System.currentTimeMillis()

            val selectedReminderType = binding.reminderValue.text.toString().toReminderType()

            val updated = original.copy(
                name = binding.subscriptionNameEditTxt.text.toString().trim(),
                price = binding.subscriptionPriceEditTxt.text.toString()
                    .replace(",", ".").toDoubleOrNull() ?: 0.0,
                currencyCode = viewModel.currencyCode.value ?: original.currencyCode,
                description = binding.descriptionValue.text.toString()
                    .takeIf { it.isNotBlank() && it != "Not specified" },
                paymentPeriod = selectedPeriodNumber,
                paymentPeriodType = selectedPeriodType,
                firstPaymentDate = newFirstPaymentDate,
                nextPaymentDate = viewModel.calculateNextPaymentDate(
                    newFirstPaymentDate,
                    selectedPeriodNumber,
                    selectedPeriodType
                ),
                logoUrl = viewModel.selectedImageFromGallery
                    ?: viewModel.selectedLogoUrl
                    ?: viewModel.selectedLogoResName?.let { "res://$it" }
                    ?: original.logoUrl,
                reminderType = selectedReminderType
            )

            val reminderChanged = selectedReminderType != original.reminderType

            lifecycleScope.launch {
                if (reminderChanged) {
                    com.anshok.subzy.util.notification.ReminderManager.cancelReminder(requireContext(), original.id)
                }

                viewModel.updateSubscription(
                    context = requireContext(),
                    original = original,
                    updated = updated,
                    onResult = { result ->
                        if (result == SaveResult.Success) {
                            findNavController().navigateUp()
                        } else {
                            showErrorDialog("Failed to update subscription")
                        }
                    }
                )
            }
        }
    }




    private fun enableEditModeWatcher() {
        binding.subscriptionNameEditTxt.doAfterTextChanged { checkChanges() }
        binding.subscriptionPriceEditTxt.doAfterTextChanged { checkChanges() }

        binding.firstPaymentValue.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            checkChanges()
        }

        binding.currencyButton.setOnClickListener {
            viewModel.loadCurrencies { currencies ->
                CurrencyPickerBottomSheet(
                    currencies = currencies,
                    currentCode = viewModel.currencyCode.value.orEmpty(),
                    onCurrencySelected = {
                        viewModel.setCurrency(it)
                        checkChanges()
                    }
                ).show(parentFragmentManager, "CurrencyPicker")
            }
        }

        binding.itemLogo.safeDelayedClick {
            galleryLauncher.launch("image/*")
            checkChanges()
        }

        binding.descriptionContainer.setOnClickListener {
            val current = originalSubscription?.description
            DescriptionBottomSheet(initialText = current) { desc ->
                binding.descriptionValue.text = if (desc.isNullOrBlank()) {
                    "Not specified"
                } else {
                    desc
                }
                checkChanges()
            }.show(parentFragmentManager, "DescriptionBottomSheet")
        }

        binding.paymentPeriodContainer.setOnClickListener {
            PaymentPeriodBottomSheet { number, periodString ->
                val type = when (periodString) {
                    "дни" -> PaymentPeriodType.DAILY
                    "недели" -> PaymentPeriodType.WEEKLY
                    "месяцы" -> PaymentPeriodType.MONTHLY
                    "годы" -> PaymentPeriodType.YEARLY
                    else -> PaymentPeriodType.MONTHLY
                }
                selectedPeriodNumber = number
                selectedPeriodType = type

                val correctForm = when (type) {
                    PaymentPeriodType.DAILY -> resources.getQuantityString(R.plurals.days, number, number)
                    PaymentPeriodType.WEEKLY -> resources.getQuantityString(R.plurals.weeks, number, number)
                    PaymentPeriodType.MONTHLY -> resources.getQuantityString(R.plurals.months, number, number)
                    PaymentPeriodType.YEARLY -> resources.getQuantityString(R.plurals.years, number, number)
                }

                binding.paymentPeriodValue.text = correctForm
                checkChanges()
            }.show(parentFragmentManager, "PaymentPeriodBottomSheet")
        }

//        binding.reminderContainer.setOnClickListener {
//            NotificationReminderBottomSheet {
//                binding.reminderValue.text = it
//                checkChanges()
//            }.show(parentFragmentManager, "NotificationReminderBottomSheet")
//        }
    }



    private fun getCurrentSubscriptionSnapshot(): Subscription? {
        val original = originalSubscription ?: return null
        val name = binding.subscriptionNameEditTxt.text?.toString()?.trim().orEmpty()
        val price = binding.subscriptionPriceEditTxt.text?.toString()
            ?.replace(",", ".")?.toDoubleOrNull() ?: return null
        val dateMillis = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            .parse(binding.firstPaymentValue.text.toString())?.time ?: return null

        val descriptionText = binding.descriptionValue.text.toString()
            .takeIf { it.isNotBlank() && it != "Not specified" }

        val reminderType = binding.reminderValue.text.toString().toReminderType()

        return original.copy(
            name = name,
            price = price,
            description = descriptionText,
            paymentPeriod = selectedPeriodNumber,
            paymentPeriodType = selectedPeriodType,
            firstPaymentDate = dateMillis,
            logoUrl = viewModel.selectedImageFromGallery
                ?: viewModel.selectedLogoUrl
                ?: viewModel.selectedLogoResName?.let { "res://$it" }
                ?: original.logoUrl,
            currencyCode = viewModel.currencyCode.value ?: original.currencyCode,
            reminderType = reminderType
        )
    }


    private fun checkChanges() {
        val current = getCurrentSubscriptionSnapshot()
        val original = originalSubscription
        val reminderChanged = binding.reminderValue.text.toString() != original?.reminderType?.toLabel()

        binding.editButtonsContainer.visibility =
            if (original != null && current != null && (current != original || reminderChanged)) {
                View.VISIBLE
            } else {
                View.GONE
            }
    }
}
