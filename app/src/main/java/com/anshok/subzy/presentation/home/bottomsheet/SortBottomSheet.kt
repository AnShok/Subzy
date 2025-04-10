package com.anshok.subzy.presentation.home.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.anshok.subzy.R
import com.anshok.subzy.databinding.BottomSheetSortBinding
import com.anshok.subzy.presentation.home.custom.SortIconView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView

/**
 * BottomSheet для выбора параметров сортировки на главном экране.
 * Позволяет пользователю выбрать тип сортировки и её направление.
 *
 * @param currentSortOption текущий выбранный тип сортировки
 * @param currentDirection текущий выбранный тип направления
 * @param onSortChanged вызывается при изменении параметров сортировки
 * @param onApply вызывается при подтверждении выбора
 */
class SortBottomSheet(
    private val currentSortOption: SortOption,
    private val currentDirection: SortDirection,
    private val onSortChanged: (SortOption, SortDirection) -> Unit,
    private val onApply: () -> Unit,
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSortBinding? = null
    private val binding get() = _binding!!

    private var selectedOption = currentSortOption
    private var selectedDirection = currentDirection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupClickListeners()
        updateCards(initial = true)
    }

    private fun setupClickListeners() {
        binding.sortDateAdd.setOnClickListener {
            onSortCardClicked(
                SortOption.DATE,
                binding.dateIcon
            )
        }
        binding.sortName.setOnClickListener { onSortCardClicked(SortOption.NAME, binding.nameIcon) }
        binding.sortPrice.setOnClickListener {
            onSortCardClicked(
                SortOption.PRICE,
                binding.priceIcon
            )
        }

        binding.applySortButton.setOnClickListener {
            onApply()
            dismiss()
        }
    }

    private fun onSortCardClicked(option: SortOption, icon: SortIconView) {
        if (selectedOption == option) {
            // Меняем направление
            selectedDirection = selectedDirection.toggle()
            icon.setDirection(selectedDirection == SortDirection.ASC, animate = true)
        } else {
            // Прячем прошлую иконку
            getIconViewByOption(selectedOption).hide(animate = true)

            // Выбираем новый тип сортировки, но сохраняем направление
            selectedOption = option
            icon.setDirection(selectedDirection == SortDirection.ASC, animate = false)
            icon.show(animate = true)
        }

        updateCards()
        onSortChanged(selectedOption, selectedDirection)

    }


    /**
     * Обновляет UI всех карточек сортировки.
     */
    private fun updateCards(initial: Boolean = false) {
        updateCard(binding.sortDateAdd, binding.dateIcon, SortOption.DATE, initial)
        updateCard(binding.sortName, binding.nameIcon, SortOption.NAME, initial)
        updateCard(binding.sortPrice, binding.priceIcon, SortOption.PRICE, initial)
    }

    /**
     * Обновляет одну карточку сортировки (фон, обводку, иконку).
     */
    private fun updateCard(
        card: MaterialCardView,
        icon: SortIconView,
        option: SortOption,
        initial: Boolean = false
    ) {
        val isSelected = option == selectedOption
        val context = requireContext()

        card.setCardBackgroundColor(
            ContextCompat.getColor(
                context,
                if (isSelected) R.color.Accent_P_100_55 else R.color.Gray_70
            )
        )

        card.strokeColor = ContextCompat.getColor(
            context,
            if (isSelected) R.color.Accent_P_100_88 else R.color.Gray_60
        )

        icon.visibility = if (isSelected) View.VISIBLE else View.GONE

        if (isSelected) {
            icon.setDirection(selectedDirection == SortDirection.ASC, animate = false)
        } else {
            icon.hide(animate = false)
        }
    }

    /**
     * Возвращает иконку сортировки по соответствующему типу.
     */
    private fun getIconViewByOption(option: SortOption): SortIconView {
        return when (option) {
            SortOption.DATE -> binding.dateIcon
            SortOption.NAME -> binding.nameIcon
            SortOption.PRICE -> binding.priceIcon
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
