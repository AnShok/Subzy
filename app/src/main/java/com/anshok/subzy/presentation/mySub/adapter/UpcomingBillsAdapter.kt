package com.anshok.subzy.presentation.mySub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.databinding.ItemUpcomingBillsBinding
import com.anshok.subzy.domain.model.Subscription
import com.anshok.subzy.util.CurrencyUtils
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class UpcomingBillsAdapter : RecyclerView.Adapter<UpcomingBillsAdapter.BillViewHolder>() {

    private var items: List<Subscription> = emptyList()

    fun submitList(list: List<Subscription>) {
        items = list.sortedBy { it.nextPaymentDate } // сортировка от ближайшей даты
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val binding =
            ItemUpcomingBillsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class BillViewHolder(private val binding: ItemUpcomingBillsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subscription: Subscription) {
            val localDate = Instant.ofEpochMilli(subscription.nextPaymentDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            val day = localDate.format(DateTimeFormatter.ofPattern("dd"))
            val month = localDate.format(DateTimeFormatter.ofPattern("MMM")).uppercase()

            binding.dayText.text = day
            binding.monthText.text = month
            binding.positionTitle.text = subscription.name
            binding.costTitle.text =
                CurrencyUtils.formatPrice(subscription.price, subscription.currencyCode)
            binding.positionTitle.isSelected = false

            // Включаем с задержкой
            binding.positionTitle.postDelayed({
                binding.positionTitle.isSelected = true
            }, 1500)
        }
    }
}