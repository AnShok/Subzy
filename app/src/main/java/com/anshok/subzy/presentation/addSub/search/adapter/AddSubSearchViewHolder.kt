package com.anshok.subzy.presentation.addSub.search.adapter

import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.databinding.SearchItemViewBinding
import com.anshok.subzy.domain.logo.model.Logo
import com.anshok.subzy.util.adapter.bindLogo
import com.anshok.subzy.util.safeDelayedClick

class AddSubSearchViewHolder(
    private val binding: SearchItemViewBinding,
    private val onItemClick: (Logo) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Logo) = with(binding) {
        positionTitle.text = item.name ?: "Unknown"
        positionTitle.isSelected = false
        positionTitle.postDelayed({ positionTitle.isSelected = true }, 1500)

        container.safeDelayedClick {
            onItemClick(item)
        }

        bindLogo(item, itemLogo)
    }

}
