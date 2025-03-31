package com.anshok.subzy.presentation.addSub.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentAddSubCreateBinding
import com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub.CategoryBottomSheetFragment
import com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub.CommentBottomSheet
import com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub.DescriptionBottomSheet
import com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub.NotificationReminderBottomSheet
import com.anshok.subzy.presentation.addSub.create.bottomSheetCreateSub.PaymentPeriodBottomSheet
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddSubCreateFragment : Fragment() {

    private val binding: FragmentAddSubCreateBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обработка нажатия кнопки "Назад"
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        // Инициализация всех кликов и настроек
        setupClickListenersAndData()

    }

    private fun setupClickListenersAndData() {
        // Открытие BottomSheet для выбора описания
        binding.descriptionContainer.setOnClickListener { openDescriptionBottomSheet() }

        // Открытие BottomSheet для выбора периода оплаты
        binding.paymentPeriodContainer.setOnClickListener { openPaymentPeriodBottomSheet() }

        // Открытие BottomSheet для выбора категории
        binding.categoryContainer.setOnClickListener {
            val bottomSheet = CategoryBottomSheetFragment { selectedCategory ->
                binding.categoryValue.text = selectedCategory
            }
            bottomSheet.show(parentFragmentManager, "CategoryBottomSheetFragment")
        }

        // Устанавливаем текущую дату в поле first_payment
        setCurrentDate()

        // Открытие календаря для выбора первой даты оплаты
        binding.firstPaymentContainer.setOnClickListener { showDatePicker() }

        // Открытие BottomSheet для выбора периодичности уведомлений
        binding.reminderContainer.setOnClickListener { openReminderBottomSheet() }

        // Открытие BottomSheet для выбора комментария
        binding.commentContainer.setOnClickListener { openCommentBottomSheet() }
    }


    private fun openDescriptionBottomSheet() {
        DescriptionBottomSheet { description ->
            if (!description.isNullOrBlank()) {
                binding.descriptionValue.text = description
            }
        }.show(parentFragmentManager, "DescriptionBottomSheet")
    }

    private fun openPaymentPeriodBottomSheet() {
        PaymentPeriodBottomSheet { number, period ->
            val correctForm = when (period) {
                "дни" -> resources.getQuantityString(R.plurals.days, number, number)
                "недели" -> resources.getQuantityString(R.plurals.weeks, number, number)
                "месяцы" -> resources.getQuantityString(R.plurals.months, number, number)
                "годы" -> resources.getQuantityString(R.plurals.years, number, number)
                else -> "$number $period"
            }
            binding.paymentPeriodValue.text = correctForm
        }.show(parentFragmentManager, "PaymentPeriodBottomSheet")
    }

    private fun setCurrentDate() {
        val formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        binding.firstPaymentValue.text = formattedDate
    }

    // Функция для отображения DatePickerDialog
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                val selectedDate =
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)
                binding.firstPaymentValue.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun openReminderBottomSheet() {
        NotificationReminderBottomSheet { selectedOption ->
            binding.reminderValue.text = selectedOption
        }.show(parentFragmentManager, "NotificationReminderBottomSheet")
    }

    private fun openCommentBottomSheet() {
        CommentBottomSheet { comment ->
            if (!comment.isNullOrBlank()) {
                binding.commentValue.text = comment
            }
        }.show(parentFragmentManager, "CommentBottomSheet")
    }
}