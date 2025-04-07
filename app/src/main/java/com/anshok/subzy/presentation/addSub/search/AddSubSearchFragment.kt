package com.anshok.subzy.presentation.addSub.search

import AddSubSearchState
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentAddSubSearchBinding
import com.anshok.subzy.presentation.addSub.search.adapter.AddSubSearchAdapter
import com.anshok.subzy.util.ResourceLogo
import com.anshok.subzy.util.safeDelayedClick
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddSubSearchFragment : Fragment() {

    private val binding: FragmentAddSubSearchBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: AddSubSearchViewModel by viewModel()

    private val adapter = AddSubSearchAdapter { logo ->
        val bundle = Bundle().apply {
            putString("logoName", logo.name)
            putString("logoUrl", logo.logoUrl)
            logo.logoResId?.let { resId ->
                putInt("logoResId", resId)
            }
        }
        findNavController().navigate(
            R.id.action_addSubSearchFragment_to_addSubCreateFragment,
            bundle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onInitialLoad()
        binding.backButton.safeDelayedClick {
            findNavController().navigateUp()
        }

        binding.createSubButton.safeDelayedClick {
            findNavController().navigate(R.id.action_addSubSearchFragment_to_addSubCreateFragment)
        }

        binding.searchRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecycleView.adapter = adapter

        // Подписка на состояния экрана
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AddSubSearchState.LocalOnly -> {
                    stopProgressBar()
                    removePlaceholders()
                    stopProgressBarNextPage()
                    adapter.setLogo(state.logos)
                }

                AddSubSearchState.Loading -> {
                    clearList()
                    removePlaceholders()
                    startProgressBar()
                    stopProgressBarNextPage()
                    //binding.searchDefaultPlaceholder.isVisible = false
                }

                is AddSubSearchState.Success -> {
                    stopProgressBar()
                    removePlaceholders()
                    stopProgressBarNextPage()
                    adapter.setLogo(state.logos)
                }

                is AddSubSearchState.NothingFound -> {
                    clearList()
                    showNothingFoundState()
                }

                is AddSubSearchState.Error -> {
                    clearList()
                    removePlaceholders()
                    //binding.searchDefaultPlaceholder.isVisible = false
                    when (state.errorType) {
                        ResourceLogo.ResponseError.NO_INTERNET -> showNoInternetState()
                        ResourceLogo.ResponseError.CLIENT_ERROR,
                        ResourceLogo.ResponseError.SERVER_ERROR -> showServerError()

                        ResourceLogo.ResponseError.NOT_FOUND -> showNothingFoundState()
                        ResourceLogo.ResponseError.UNKNOWN_ERROR -> showMessage(getString(R.string.unknown_error))
                    }
                }

                AddSubSearchState.LoadingNextPage -> {
                    binding.progressBarNextPage.isVisible = true
                }

                is AddSubSearchState.ErrorNextPage -> {
                    stopProgressBarNextPage()
                    showMessage(getString(R.string.next_page_server_error))
                }

                is AddSubSearchState.LoadNextPage -> {
                    adapter.setLogo(state.logos)
                    stopProgressBarNextPage()
                }

                is AddSubSearchState.NoMoreData -> {
                    stopProgressBarNextPage()
                    showMessage(getString(R.string.no_more_data_add_manually))
                }


                else -> {
                    showMessage(getString(R.string.unknown_error))
                }
            }
        }


        // Подписка на список логотипов
        viewModel.logos.observe(viewLifecycleOwner) { logos ->
            adapter.setLogo(logos)
            // binding.searchDefaultPlaceholder.isGone = logos.isNotEmpty()
        }

        binding.searchQuery.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.searchIconLoupe.isVisible = false
                binding.clearCrossIc.isVisible = true
                //binding.searchDefaultPlaceholder.isVisible = false
                viewModel.onQueryChanged(text.toString())
            } else {
                binding.searchIconLoupe.isVisible = true
                binding.clearCrossIc.isVisible = false
                //binding.searchDefaultPlaceholder.isVisible = true
                viewModel.onQueryChanged("")
            }
        }

        binding.clearCrossIc.safeDelayedClick {
            binding.searchQuery.text.clear()
        }

        binding.searchQuery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
            }
            false
        }

        binding.searchRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos = (binding.searchRecycleView.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()
                    if (pos >= adapter.itemCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }
        })
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun clearList() {
        Log.d("SearchUI", "Clearing adapter list")
        adapter.setLogo(emptyList())
    }

    private fun removePlaceholders() {
        binding.noSubToShow.isVisible = false
        binding.noSubToShowText.isVisible = false
        binding.noConnectionText.isVisible = false
        binding.noConnectionPlaceholder.isVisible = false
        binding.serverError.isVisible = false
        binding.serverErrorText.isVisible = false
    }

    private fun startProgressBar() {
        Log.d("SearchUI", "Showing progressBar")
        binding.progressBar.isVisible = true
    }

    private fun stopProgressBar() {
        Log.d("SearchUI", "Hiding progressBar")
        binding.progressBar.isVisible = false
    }

    private fun stopProgressBarNextPage() {
        Log.d("SearchUI", "Hiding progressBar")
        binding.progressBarNextPage.isVisible = false
    }

    private fun showNoInternetState() {
        stopProgressBar()
        binding.noConnectionPlaceholder.isVisible = true
        binding.noConnectionText.isVisible = true
    }

    private fun showServerError() {
        stopProgressBar()
        binding.serverError.isVisible = true
        binding.serverErrorText.isVisible = true
    }

    private fun showNothingFoundState() {
        stopProgressBar()
        binding.noSubToShow.isVisible = true
        binding.noSubToShowText.isVisible = true
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setTextColor(requireContext().getColor(R.color.white))
            .setBackgroundTint(requireContext().getColor(R.color.Accent_P_100))
            .show()
    }
}
