package com.anshok.subzy.presentation.settings

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.anshok.subzy.R
import com.anshok.subzy.databinding.DialogRestartAppBinding

class RestartDialogFragment(private val onComplete: () -> Unit) : DialogFragment() {

    private var _binding: DialogRestartAppBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRestartAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) onComplete()
        }, 1500)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}


