package com.anshok.subzy.presentation.settings.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anshok.subzy.R
import com.anshok.subzy.domain.model.AppTheme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ThemeBottomSheet(
    private val selectedTheme: AppTheme,
    private val onThemeSelected: (AppTheme) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_theme, container, false)

        val options = listOf(
            Triple(R.id.optionLight, AppTheme.LIGHT, view.findViewById<TextView>(R.id.optionLight)),
            Triple(R.id.optionDark, AppTheme.DARK, view.findViewById<TextView>(R.id.optionDark)),
            Triple(
                R.id.optionSystem,
                AppTheme.SYSTEM,
                view.findViewById<TextView>(R.id.optionSystem)
            )
        )

        options.forEach { (_, theme, textView) ->
            if (theme == selectedTheme) {
                textView.setTextColor(requireContext().getColor(R.color.Accent_P_100))
            } else {
                textView.setTextColor(requireContext().getColor(android.R.color.white))
            }

            textView.setOnClickListener {
                onThemeSelected(theme)
                dismiss()
            }
        }

        return view
    }
}
