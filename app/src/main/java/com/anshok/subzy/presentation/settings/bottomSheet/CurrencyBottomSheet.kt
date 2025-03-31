package com.anshok.subzy.presentation.settings.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.databinding.BottomSheetCurrencySelectorBinding
import com.anshok.subzy.domain.model.CurrencyRate
import com.anshok.subzy.presentation.settings.adapter.CurrencyAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CurrencyBottomSheet(
    private val currencies: List<CurrencyRate>,
    private val currentCode: String,
    private val onCurrencySelected: (String) -> Unit
) : BottomSheetDialogFragment() {

    private val binding: BottomSheetCurrencySelectorBinding by viewBinding(CreateMethod.INFLATE)
    private lateinit var adapter: CurrencyAdapter
    override fun getTheme(): Int = com.anshok.subzy.R.style.BottomSheetDialogResizeStyle


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = CurrencyAdapter(currencies, currentCode) { code ->
            onCurrencySelected(code)
            dismiss()
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.searchEditText.doAfterTextChanged { query ->
            adapter.filter(query.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
        }
    }
}
