package com.anshok.subzy.presentation.addSub.search.adapter

import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.R
import com.anshok.subzy.databinding.SearchItemViewBinding
import com.anshok.subzy.domain.model.Logo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.anshok.subzy.util.adapter.bindLogo

class AddSubSearchViewHolder(
    private val binding: SearchItemViewBinding,
    private val onItemClick: (Logo) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Logo) = with(binding) {
        positionTitle.text = item.name ?: "Unknown"
        positionTitle.isSelected = false
        positionTitle.postDelayed({ positionTitle.isSelected = true }, 1500)

        container.setOnClickListener { onItemClick(item) }

        val logo = when {
            !item.logoUrl.isNullOrBlank() -> item.logoUrl
            item.logoResId != null        -> "res://${itemView.resources.getResourceEntryName(item.logoResId!!)}"
            else                          -> null
        }

        bindLogo(logo, itemLogo)
    }

}
