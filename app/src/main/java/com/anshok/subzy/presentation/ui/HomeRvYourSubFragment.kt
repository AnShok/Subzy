package com.anshok.subzy.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.databinding.FragmentHomeRvYourSubBinding
import com.anshok.subzy.util.adapter.SubscriptionsAdapter

class HomeRvYourSubFragment : Fragment() {

    // Используем ViewBinding
    private val binding: FragmentHomeRvYourSubBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Возвращаем корневой элемент через ViewBinding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация RecyclerView через binding
        binding.subscriptionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.subscriptionsList.adapter = SubscriptionsAdapter()
    }
}