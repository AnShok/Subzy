package com.anshok.subzy.presentation.common

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.anshok.subzy.R

class CustomErrorDialogFragment(
    private val title: String = "Ошибка",
    private val message: String,
    private val onDismiss: (() -> Unit)? = null
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.CustomAlertDialog).apply {
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
        val view = inflater.inflate(R.layout.dialog_custom_error, container, false)

        val titleText = view.findViewById<TextView>(R.id.titleText)
        val messageText = view.findViewById<TextView>(R.id.messageText)
        val okButton = view.findViewById<View>(R.id.okButton)

        titleText.text = title
        messageText.text = message

        okButton.setOnClickListener {
            dismiss()
            onDismiss?.invoke()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
