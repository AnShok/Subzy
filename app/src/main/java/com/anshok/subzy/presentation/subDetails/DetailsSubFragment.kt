package com.anshok.subzy.presentation.subDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.databinding.FragmentDetailsSubBinding
import com.anshok.subzy.presentation.subDetails.adapter.SubscriptionDetailAdapter
import com.anshok.subzy.util.CurrencyUtils
import com.anshok.subzy.util.adapter.bindLogo
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

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.deleteButton.setOnClickListener {
            viewModel.deleteSubscription {
                findNavController().navigateUp()
            }
        }
    }
}
