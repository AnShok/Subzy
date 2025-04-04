package com.anshok.subzy.presentation.mySub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.databinding.FragmentMySubItemRvBinding
import com.anshok.subzy.presentation.mySub.adapter.SubscriptionsAdapter
import com.anshok.subzy.presentation.mySub.viewmodel.MySubViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RvItemSubFragment : Fragment() {

    private val binding: FragmentMySubItemRvBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: MySubViewModel by viewModel()
    private lateinit var adapter: SubscriptionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = SubscriptionsAdapter { subscription ->
            val action =
                MySubFragmentDirections.actionMySubFragmentToDetailsSubFragment(subscription.id)

            // Навигация через родителя
            (parentFragment?.findNavController() ?: findNavController()).navigate(action)
        }

        binding.subscriptionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.subscriptionsList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.subscriptions.collect { list ->
                adapter.submitList(list)

                val isEmpty = list.isEmpty()
                binding.subscriptionsList.visibility = if (isEmpty) View.GONE else View.VISIBLE
                binding.placeholderGroup.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        }
    }
}
