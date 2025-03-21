package com.anshok.subzy.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.databinding.FragmentUpcomingBillsBinding
import com.anshok.subzy.util.adapter.UpcomingBillsAdapter

class HomeRvUpcomingBillsFragment : Fragment() {

    // Используем ViewBinding
    private val binding: FragmentUpcomingBillsBinding by viewBinding(CreateMethod.INFLATE)

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
        binding.billsList.layoutManager = LinearLayoutManager(requireContext())
        binding.billsList.adapter = UpcomingBillsAdapter()
    }
}