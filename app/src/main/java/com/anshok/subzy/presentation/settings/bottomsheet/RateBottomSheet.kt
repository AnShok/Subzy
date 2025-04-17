package com.anshok.subzy.presentation.settings.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.BottomSheetRateBinding
import com.anshok.subzy.presentation.settings.viewmodel.RateViewModel
import com.anshok.subzy.util.safeDelayedAction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RateBottomSheet : BottomSheetDialogFragment() {

    private val binding: BottomSheetRateBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: RateViewModel by viewModel()
    private var currentRating = 0
    private lateinit var starViews: List<ImageView>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        starViews = listOf(
            binding.star1,
            binding.star2,
            binding.star3,
            binding.star4,
            binding.star5
        )

        starViews.forEachIndexed { index, star ->
            star.setOnClickListener { handleStarClick(index + 1) }
        }

        viewModel.showFeedbackSheet.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                FeedbackBottomSheet().show(parentFragmentManager, "FeedbackBottomSheet")
                dismiss()
            }
        }

        viewModel.openPlayMarket.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                startActivity(it)
                dismiss()
            }
        }

    }

    private fun handleStarClick(rating: Int) {
        currentRating = rating
        updateStars(rating)

        // Добавим задержку 600 мс перед отправкой события
        binding.starContainer.safeDelayedAction(600) {
            viewModel.onStarSelected(rating)
        }
    }


    private fun updateStars(rating: Int) {
        starViews.forEachIndexed { index, imageView ->
            val filled = index < rating
            imageView.setImageResource(if (filled) R.drawable.ic_rate_filled else R.drawable.ic_rate)

            if (filled) {
                imageView.alpha = 0f
                imageView.scaleX = 0.8f
                imageView.scaleY = 0.8f
                imageView.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start()
            }
        }
    }
}
