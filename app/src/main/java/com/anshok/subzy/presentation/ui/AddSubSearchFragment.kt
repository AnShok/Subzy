package com.anshok.subzy.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentAddSubSearchBinding

class AddSubSearchFragment : Fragment() {

    private val binding: FragmentAddSubSearchBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обработка нажатия кнопки "Назад"
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.addSubButton.setOnClickListener {
            findNavController().navigate(R.id.action_addSubSearchFragment_to_addSubCreateFragment)
        }
        binding.createSubButton.setOnClickListener {
            findNavController().navigate(R.id.action_addSubSearchFragment_to_addSubCreateFragment)
        }
    }
}