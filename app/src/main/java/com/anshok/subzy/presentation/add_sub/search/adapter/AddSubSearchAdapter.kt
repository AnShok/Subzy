package com.anshok.subzy.presentation.add_sub.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.databinding.SearchItemViewBinding
import com.anshok.subzy.domain.model.Logo

class AddSubSearchAdapter(
    private val onItemClick: (Logo) -> Unit
) : RecyclerView.Adapter<AddSubSearchViewHolder>() {

    private val items = mutableListOf<Logo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddSubSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchItemViewBinding.inflate(inflater, parent, false)
        return AddSubSearchViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: AddSubSearchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setLogo(newData: List<Logo>) {
        val diffCallback = AddSubSearchDiffCallback(items, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
}
