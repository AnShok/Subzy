package com.anshok.subzy.presentation.subDetails

//import com.anshok.subzy.presentation.subDetails.adapter.SubscriptionDetailAdapter
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentDetailsSubBinding
import com.anshok.subzy.presentation.subDetails.dialog.DeleteConfirmationDialog
import com.anshok.subzy.util.CurrencyUtils
import com.anshok.subzy.util.adapter.bindLogo
import com.anshok.subzy.util.adapter.toLogo
import com.anshok.subzy.util.animation.animateAppear
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.snackbar.Snackbar
import marqueeOnceThenFadeToEllipsizeEnd
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsSubFragment : Fragment() {

    private val binding: FragmentDetailsSubBinding by viewBinding(CreateMethod.INFLATE)
    private val args: DetailsSubFragmentArgs by navArgs()
    private val viewModel: DetailsSubViewModel by viewModel()

    private var isDescriptionExpanded = false

    val Int.dp: Int get() = (this * resources.displayMetrics.density).toInt()
    val Float.dp: Float get() = this * resources.displayMetrics.density


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.alpha = 0f
        binding.root.animate().alpha(1f).setDuration(250).start()

        viewModel.loadSubscriptionDetails(args.subscriptionId)

        viewModel.subscription.observe(viewLifecycleOwner) { subscription ->
            subscription?.let {
                binding.subscriptionName.text = it.name
                binding.subscriptionPrice.text =
                    CurrencyUtils.formatPrice(it.price, it.currencyCode)
                bindLogo(it.logoUrl.toLogo(requireContext()), binding.itemLogo)

                val description = it.description.orEmpty()
                if (description.isBlank()) {
                    binding.descriptionValue.text = "Not specified"
                    binding.descriptionArrow.isVisible = true
                } else {
                    binding.descriptionValue.text = description
                    binding.descriptionArrow.isVisible = true
                    binding.descriptionValue.maxLines = 1
                    binding.descriptionValue.ellipsize = android.text.TextUtils.TruncateAt.END
                }

                binding.firstPaymentValue.text = viewModel.formatDate(it.firstPaymentDate)

                val nextPaymentMillis = viewModel.getNextPaymentDateMillis(
                    it.firstPaymentDate,
                    it.paymentPeriod,
                    it.paymentPeriodType
                )
                binding.nextPaymentValue.text = viewModel.formatDate(nextPaymentMillis)

                binding.paymentPeriodValue.text =
                    "${it.paymentPeriod} ${it.paymentPeriodType.name.lowercase()}"
                binding.reminderValue.text =
                    "Not specified" // TODO: подтянуть из ReminderRepository

                val payments = viewModel.calculateTotalPaidAmount(
                    it.firstPaymentDate,
                    it.paymentPeriod,
                    it.paymentPeriodType
                )
                val totalPaid = it.price * payments
                binding.totalPaidValue.text = CurrencyUtils.formatPrice(totalPaid, it.currencyCode)

                binding.detailsCard.animateAppear()
            }
        }

        binding.menuButton.safeDelayedClick {
            showCustomPopupMenu(binding.menuButton)
        }

        binding.subscriptionName.marqueeOnceThenFadeToEllipsizeEnd(viewLifecycleOwner)
        binding.subscriptionPrice.marqueeOnceThenFadeToEllipsizeEnd(viewLifecycleOwner)

        binding.descriptionContainer.setOnClickListener {
            isDescriptionExpanded = !isDescriptionExpanded
            toggleDescriptionExpansion(isDescriptionExpanded)
        }
    }

    private fun toggleDescriptionExpansion(expanded: Boolean) {
        val arrow = binding.descriptionArrow
        val card =
            binding.descriptionValue.parent as com.google.android.material.card.MaterialCardView
        val value = binding.descriptionValue

        // Анимация стрелки
        val rotate = RotateAnimation(
            if (expanded) 0f else 180f,
            if (expanded) 180f else 0f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 250
            fillAfter = true
        }
        arrow.startAnimation(rotate)

        // Плавный layout transition
        //androidx.transition.TransitionManager.beginDelayedTransition(binding.descriptionContainer)

        val transition = androidx.transition.AutoTransition().apply {
            duration = 250
        }
        androidx.transition.TransitionManager.beginDelayedTransition(
            binding.descriptionContainer,
            transition
        )


        // Обновление layout-параметров карточки
        val cardParams = card.layoutParams as ConstraintLayout.LayoutParams
        cardParams.apply {
            width = if (expanded) ViewGroup.LayoutParams.MATCH_PARENT else 0

            if (expanded) {
                topToTop = ConstraintLayout.LayoutParams.UNSET
                bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                topToBottom = binding.descriptionLabel.id
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            } else {
                topToBottom = ConstraintLayout.LayoutParams.UNSET
                topToTop = binding.descriptionLabel.id
                bottomToBottom = binding.descriptionLabel.id
                startToStart = ConstraintLayout.LayoutParams.UNSET
                endToEnd = ConstraintLayout.LayoutParams.UNSET
                startToEnd = binding.descriptionLabel.id
                endToStart = binding.descriptionArrow.id
            }

            // Меняем отступы
            marginStart = if (expanded) 0 else 8.dp
            marginEnd = if (expanded) 8.dp else 8.dp
            topMargin = if (expanded) 8.dp else 0
        }
        card.layoutParams = cardParams

        // Обновление внешнего вида карточки
        card.apply {
            radius = if (expanded) 20f.dp else 0f.dp
            strokeWidth = if (expanded) 1.dp else 0
            setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (expanded) R.color.Gray_75 else android.R.color.transparent
                )
            )
        }

        // Настройка текста
        value.apply {
            maxLines = if (expanded) Int.MAX_VALUE else 1
            ellipsize = if (expanded) null else android.text.TextUtils.TruncateAt.END
            gravity = if (expanded) Gravity.START else Gravity.END
            setPadding(
                if (expanded) 8.dp else 4.dp,
                if (expanded) 8.dp else 0,
                if (expanded) 8.dp else 4.dp,
                if (expanded) 8.dp else 0
            )
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

        // Измеряем popupView перед показом
        popupView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        val popupWidth = popupView.measuredWidth

        // Показ под якорем
        val offsetX = -popupWidth
        val offsetY = anchor.height / 10

        popupWindow.showAsDropDown(anchor, offsetX, offsetY)

        // Обработчики кликов
        val editOption = popupView.findViewById<LinearLayout>(R.id.edit_option)
        val deleteOption = popupView.findViewById<LinearLayout>(R.id.delete_option)

        editOption.setOnClickListener {
            popupWindow.dismiss()
            val bundle = Bundle().apply {
                putString("subscriptionId", args.subscriptionId.toString())
                putBoolean("isEdit", true)
            }
            findNavController().navigate(R.id.addSubCreateFragment, bundle)
        }

        deleteOption.setOnClickListener {
            popupWindow.dismiss()
            DeleteConfirmationDialog {
                viewModel.deleteSubscription {
                    Snackbar.make(binding.root, "Subscription deleted", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.Accent_P_100
                            )
                        )
                        .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        .show()
                    findNavController().navigateUp()

                }
            }.show(parentFragmentManager, "DeleteConfirmationDialog")
        }
    }
}
