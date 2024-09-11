package com.anshok.subzy.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.databinding.ItemUpcomingBillsBinding

class UpcomingBillsAdapter : RecyclerView.Adapter<UpcomingBillsAdapter.BillViewHolder>() {

    // Пример статического списка данных для заглушки
    private val date = listOf(
        "07",
        "11",
        "17",
        "23",
        "28",
        "30"
    )

    // Пример статического списка данных для заглушки
    private val month = listOf(
        "JAN",
        "MAR",
        "MAR",
        "APR",
        "MAY",
        "DEC"
    )

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val binding =
            ItemUpcomingBillsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        holder.bind(subscriptions[position], costs[position], month[position], date[position])
    }

    override fun getItemCount(): Int {
        return subscriptions.size
    }

    class BillViewHolder(private val binding: ItemUpcomingBillsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subscriptions: String, cost: String, month: String, date: String) {
            binding.positionTitle.text = subscriptions
            binding.costTitle.text = "$$cost"
            binding.monthText.text = month
            binding.dayText.text = date
        }
    }
}