package com.anshok.subzy.presentation.subDetails

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentDetailsSubBinding
import com.anshok.subzy.presentation.subDetails.adapter.SubscriptionDetailAdapter
import com.anshok.subzy.presentation.subDetails.dialog.DeleteConfirmationDialog
import com.anshok.subzy.util.CurrencyUtils
import com.anshok.subzy.util.adapter.bindLogo
import com.anshok.subzy.util.safeDelayedAction
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsSubFragment : Fragment() {

    private val binding: FragmentDetailsSubBinding by viewBinding(CreateMethod.INFLATE)
    private val args: DetailsSubFragmentArgs by navArgs()
    private val viewModel: DetailsSubViewModel by viewModel()
    private lateinit var adapter: SubscriptionDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SubscriptionDetailAdapter()
        binding.detailsList.adapter = adapter

        viewModel.loadSubscriptionDetails(args.subscriptionId)

        viewModel.subscription.observe(viewLifecycleOwner, Observer { subscription ->
            subscription?.let {
                binding.subscriptionName.text = it.name
                binding.subscriptionPrice.text =
                    CurrencyUtils.formatPrice(it.price, it.currencyCode)
                bindLogo(it.logoUrl, binding.itemLogo)
            }
        })

        viewModel.details.observe(viewLifecycleOwner) { adapter.submitList(it) }

        binding.backButton.safeDelayedClick {
            findNavController().navigateUp()
        }

        binding.menuButton.safeDelayedClick {
            showCustomPopupMenu(binding.menuButton)
        }

        binding.subscriptionName.safeDelayedAction(2000) {
            binding.subscriptionName.isSelected = true
        }

    }

    private fun showCustomPopupMenu(anchor: View) {
        val popupView = layoutInflater.inflate(R.layout.popup_subscription_menu, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(null)
        popupWindow.elevation = 16f
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.animationStyle = R.style.PopupWindowAnimation

        // 🔹 Измеряем popupView перед показом
        popupView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        val popupWidth = popupView.measuredWidth

        // 🔹 Показ под якорем, но сдвигаем влево на ширину popup - ширина anchor
        val offsetX = -popupWidth
        val offsetY = anchor.height /10

        // 🔹 Используем showAsDropDown, а не showAtLocation
        popupWindow.showAsDropDown(anchor, offsetX, offsetY)

        // 🔹 Обработчики кликов
        val editOption = popupView.findViewById<LinearLayout>(R.id.edit_option)
        val deleteOption = popupView.findViewById<LinearLayout>(R.id.delete_option)

        editOption.setOnClickListener {
            popupWindow.dismiss()
            // TODO: Навигация на экран редактирования
        }

        deleteOption.setOnClickListener {
            popupWindow.dismiss()
            DeleteConfirmationDialog {
                viewModel.deleteSubscription {
                    Snackbar.make(binding.root, "Subscription deleted", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.Accent_P_100))
                        .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        .show()
                    findNavController().navigateUp()

                }
            }.show(parentFragmentManager, "DeleteConfirmationDialog")
        }
    }
}
