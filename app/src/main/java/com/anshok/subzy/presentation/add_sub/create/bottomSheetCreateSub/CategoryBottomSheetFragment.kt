package com.anshok.subzy.presentation.add_sub.create.bottomSheetCreateSub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentCategoryBottomSheetBinding
import com.anshok.subzy.util.adapter.Category
import com.anshok.subzy.util.adapter.CategoryAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryBottomSheetFragment(
    private val onCategorySelected: (String) -> Unit
) : BottomSheetDialogFragment() {

    private val binding: FragmentCategoryBottomSheetBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = listOf(
            Category("Auto & Transport", R.drawable.ic_auto_transport, R.color.Accent_S_100),
            Category("Entertainment", R.drawable.ic_entertainment, R.color.Accent_P_100),
            Category("Security", R.drawable.ic_security, R.color.Primary_100)
        )

        // Устанавливаем адаптер для RecyclerView
        val adapter = CategoryAdapter(categories) { category ->
            onCategorySelected(category.name)
            dismiss()
        }
        binding.categoryList.layoutManager = LinearLayoutManager(context)
        binding.categoryList.adapter = adapter

        // Обработка кнопки добавления новой категории
        binding.addCategoryBtnBottomSheet.setOnClickListener {
            // Логика добавления новой категории
            onCategorySelected("Новая категория")
            dismiss()
        }
    }
}
