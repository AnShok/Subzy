package com.anshok.subzy.presentation.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.R
import com.anshok.subzy.databinding.ItemCurrencyBinding
import com.anshok.subzy.domain.model.CurrencyRate

class CurrencyAdapter(
    private val allCurrencies: List<CurrencyRate>,
    private var selectedCode: String,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private var filteredList = allCurrencies.toMutableList()

    inner class ViewHolder(val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CurrencyRate) {
            binding.code.text = item.code
            binding.name.text = item.name

            val isSelected = item.code == selectedCode

            // Анимация галочки
            binding.selectedIndicator.apply {
                if (isSelected) {
                    isVisible = true
                    alpha = 0f
                    animate().alpha(1f).setDuration(200).start()
                } else {
                    animate().alpha(0f).setDuration(150).withEndAction { isVisible = false }.start()
                }
            }

            // Анимация фона
            binding.root.background = ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.bg_currency_item_selector
            )
            binding.root.isSelected = isSelected

            binding.root.setOnClickListener {
                val oldCode = selectedCode
                selectedCode = item.code
                notifyItemChanged(filteredList.indexOfFirst { it.code == oldCode })
                notifyItemChanged(adapterPosition)
                onItemClick(item.code)
            }
        }
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            allCurrencies.toMutableList()
        } else {
            allCurrencies.filter {
                it.code.contains(query, true) || it.name.contains(query, true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = filteredList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(filteredList[position])
}
