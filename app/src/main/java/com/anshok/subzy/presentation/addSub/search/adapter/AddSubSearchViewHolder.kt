package com.anshok.subzy.presentation.addSub.search.adapter

import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.R
import com.anshok.subzy.databinding.SearchItemViewBinding
import com.anshok.subzy.domain.model.Logo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AddSubSearchViewHolder(
    private val binding: SearchItemViewBinding,
    private val onItemClick: (Logo) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Logo) = with(binding) {
        // Отображаем имя сервиса
        positionTitle.text = item.name ?: "Unknown"
        positionTitle.isSelected = false

        // Включаем с задержкой
        positionTitle.postDelayed({
            positionTitle.isSelected = true
        }, 1500)

        container.setOnClickListener {
            onItemClick(item)
        }

        val logoSource = when {
            item.logoUrl != null -> item.logoUrl
            item.logoResId != null -> item.logoResId
            else -> R.drawable.ic_placeholder_30px
        }

        // Загружаем логотип через Glide
        Glide.with(itemView)
            .load(logoSource)
            .placeholder(R.drawable.ic_placeholder_30px)
            .error(R.drawable.ic_placeholder_30px)
            .centerCrop()
            .transform(
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.dimen_12dp))
            )
            .into(itemLogo)

    }
}
