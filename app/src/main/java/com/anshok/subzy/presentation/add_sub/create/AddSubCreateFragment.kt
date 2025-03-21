package com.anshok.subzy.presentation.add_sub.create

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
import com.anshok.subzy.presentation.bottomSheetCreateSub.CategoryBottomSheetFragment
import com.anshok.subzy.presentation.bottomSheetCreateSub.CommentBottomSheet
import com.anshok.subzy.presentation.bottomSheetCreateSub.DescriptionBottomSheet
import com.anshok.subzy.presentation.bottomSheetCreateSub.NotificationReminderBottomSheet
import com.anshok.subzy.presentation.bottomSheetCreateSub.PaymentPeriodBottomSheet
import java.text.SimpleDateFormat
import java.util.Calendar
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
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Инициализация всех кликов и настроек
        setupClickListenersAndData()
    }

    private fun setupClickListenersAndData() {
        // Открытие BottomSheet для выбора описания
        binding.descriptionContainer.setOnClickListener {
            openDescriptionBottomSheet()
        }

        // Открытие BottomSheet для выбора периода оплаты
        binding.paymentPeriodContainer.setOnClickListener {
            openPaymentPeriodBottomSheet()
        }

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
        binding.firstPaymentContainer.setOnClickListener {
            showDatePicker()
        }

        // Открытие BottomSheet для выбора периодичности уведомлений
        binding.reminderContainer.setOnClickListener {
            openReminderBottomSheet()
        }

        // Открытие BottomSheet для выбора комментария
        binding.commentContainer.setOnClickListener {
            openCommentBottomSheet()
        }
    }


    private fun openDescriptionBottomSheet() {
        val bottomSheet = DescriptionBottomSheet { description ->
            if (description != null && description.isNotBlank()) {
                binding.descriptionValue.text = description
            }
        }
        bottomSheet.show(parentFragmentManager, "DescriptionBottomSheet")
    }

    private fun openPaymentPeriodBottomSheet() {
        val bottomSheet = PaymentPeriodBottomSheet { number, period ->
            val correctForm = when (period) {
                "дни" -> resources.getQuantityString(R.plurals.days, number, number)
                "недели" -> resources.getQuantityString(R.plurals.weeks, number, number)
                "месяцы" -> resources.getQuantityString(R.plurals.months, number, number)
                "годы" -> resources.getQuantityString(R.plurals.years, number, number)
                else -> "$number $period"
            }
            binding.paymentPeriodValue.text = correctForm
        }
        bottomSheet.show(parentFragmentManager, "PaymentPeriodBottomSheet")
    }

    private fun setCurrentDate() {
        // Получаем текущую дату
        val currentDate = Calendar.getInstance().time
        // Форматируем дату в строку
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        // Устанавливаем дату в текстовое поле
        binding.firstPaymentValue.text = formattedDate
    }

    // Функция для отображения DatePickerDialog
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Установка выбранной даты в TextView с использованием SimpleDateFormat
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val selectedDate = dateFormat.format(calendar.time)
                binding.firstPaymentValue.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun openReminderBottomSheet() {
        val bottomSheet = NotificationReminderBottomSheet { selectedOption ->
            binding.reminderValue.text = selectedOption
        }
        bottomSheet.show(parentFragmentManager, "NotificationReminderBottomSheet")
    }

    private fun openCommentBottomSheet() {
        val bottomSheet = CommentBottomSheet { comment ->
            if (comment != null && comment.isNotBlank()) {
                binding.commentValue.text = comment
            }
        }
        bottomSheet.show(parentFragmentManager, "CommentBottomSheet")
    }
}