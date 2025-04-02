package com.anshok.subzy.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.R
import com.anshok.subzy.databinding.ItemSubscriptionBinding

class SubscriptionsAdapter : RecyclerView.Adapter<SubscriptionsAdapter.SubscriptionViewHolder>() {

    // Пример статического списка данных для заглушки
    private val subscriptions = listOf(
        "Spotify",
        "YouTube Premium",
        "Microsoft OneDrive",
        "Spotify",
        "YouTube Premium",
        "Microsoft OneDrive"
    )

    // Пример статического списка данных для заглушки
    private val costs = listOf(
        "29.99",
        "5.99",
        "17",
        "10.15",
        "9.99",
        "205.05"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val binding =
            ItemSubscriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        holder.bind(subscriptions[position], costs[position])
    }

    override fun getItemCount(): Int {
        return subscriptions.size
    }

    class SubscriptionViewHolder(private val binding: ItemSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subscription: String, cost: String) {
            binding.itemLogo.setImageResource(R.drawable.logo_spotify)
            binding.positionTitle.text = subscription
            binding.costTitle.text = "$$cost"
        }
    }
}