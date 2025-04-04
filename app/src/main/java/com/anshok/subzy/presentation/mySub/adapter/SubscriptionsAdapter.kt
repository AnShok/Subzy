package com.anshok.subzy.presentation.mySub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.R
import com.anshok.subzy.databinding.ItemSubscriptionBinding
import com.anshok.subzy.domain.model.Subscription
import com.anshok.subzy.util.CurrencyUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.anshok.subzy.util.adapter.bindLogo

class SubscriptionsAdapter(
    private val onItemClick: (Subscription) -> Unit
) : RecyclerView.Adapter<SubscriptionsAdapter.SubscriptionViewHolder>() {

    private var items: List<Subscription> = emptyList()

    fun submitList(list: List<Subscription>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val binding =
            ItemSubscriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SubscriptionViewHolder(private val binding: ItemSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Subscription) {
            binding.positionTitle.text = item.name
            binding.costTitle.text = CurrencyUtils.formatPrice(item.price, item.currencyCode)
            binding.root.setOnClickListener { onItemClick(item) }
            binding.positionTitle.isSelected = false

            binding.positionTitle.postDelayed({
                binding.positionTitle.isSelected = true
            }, 1500)

            bindLogo(item.logoUrl, binding.itemLogo)
        }
    }
}
