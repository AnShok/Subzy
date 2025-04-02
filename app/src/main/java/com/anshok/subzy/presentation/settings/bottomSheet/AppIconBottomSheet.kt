package com.anshok.subzy.presentation.settings.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.databinding.BottomSheetAppIconBinding
import com.anshok.subzy.domain.model.AppIconStyle
import com.anshok.subzy.presentation.settings.RestartDialogFragment
import com.anshok.subzy.presentation.settings.adapter.AppIconAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AppIconBottomSheet(
    private val selectedStyle: AppIconStyle,
    private val onIconSelected: (AppIconStyle) -> Unit
) : BottomSheetDialogFragment() {

    private val binding: BottomSheetAppIconBinding by viewBinding(CreateMethod.INFLATE)
    override fun getTheme(): Int = com.anshok.subzy.R.style.BottomSheetDialogResizeStyle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AppIconAdapter(AppIconStyle.values().toList(), selectedStyle) { selected ->
            RestartDialogFragment {
                onIconSelected(selected)
            }.show(parentFragmentManager, "RestartDialog")
            dismiss()
        }


        binding.iconRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.iconRecyclerView.adapter = adapter
    }
}
