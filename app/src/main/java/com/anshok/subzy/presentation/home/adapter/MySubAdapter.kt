package com.anshok.subzy.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.databinding.ItemSubscriptionBinding
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.util.CurrencyUtils
import com.anshok.subzy.util.adapter.bindLogo
import com.anshok.subzy.util.adapter.toLogo
import com.anshok.subzy.util.safeDelayedAction
import com.anshok.subzy.util.safeDelayedClick

/**
 * Адаптер для отображения списка подписок на главном экране.
 * Использует ListAdapter с DiffUtil для оптимизированного обновления.
 *
 * @param onItemClick колбэк при нажатии на элемент подписки
 */
class MySubAdapter(
    private val onItemClick: (Subscription) -> Unit
) : ListAdapter<Subscription, MySubAdapter.SubscriptionViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val binding =
            ItemSubscriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SubscriptionViewHolder(private val binding: ItemSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Subscription) {
            binding.positionTitle.text = item.name
            binding.costTitle.text = CurrencyUtils.formatPrice(item.price, item.currencyCode)
            binding.container.safeDelayedClick { onItemClick(item) }

            binding.positionTitle.isSelected = false
            binding.positionTitle.safeDelayedAction(1500) {
                binding.positionTitle.isSelected = true
            }

            // Загрузка логотипа (URL / ресурс / галерея)
            bindLogo(item.logoUrl.toLogo(binding.root.context), binding.itemLogo)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Subscription>() {
        override fun areItemsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
            return oldItem == newItem
        }
    }
}
