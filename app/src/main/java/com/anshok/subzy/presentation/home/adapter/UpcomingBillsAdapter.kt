package com.anshok.subzy.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.databinding.ItemUpcomingBillsBinding
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.util.CurrencyUtils
import com.anshok.subzy.util.safeDelayedAction
import com.anshok.subzy.util.safeDelayedClick
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class UpcomingBillsAdapter(
    private val onItemClick: (Subscription) -> Unit
) : RecyclerView.Adapter<UpcomingBillsAdapter.BillViewHolder>() {

    private var items: List<Subscription> = emptyList()

    fun submitList(list: List<Subscription>) {
        items = list.sortedBy { it.nextPaymentDate }
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

    inner class BillViewHolder(private val binding: ItemUpcomingBillsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Subscription) {
            val localDate = Instant.ofEpochMilli(item.nextPaymentDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            binding.dayText.text = localDate.format(DateTimeFormatter.ofPattern("dd"))
            binding.monthText.text =
                localDate.format(DateTimeFormatter.ofPattern("MMM")).uppercase()

            binding.positionTitle.text = item.name
            binding.costTitle.text = CurrencyUtils.formatPrice(item.price, item.currencyCode)

            binding.positionTitle.isSelected = false
            binding.positionTitle.safeDelayedAction(1500) {
                binding.positionTitle.isSelected = true
            }

            binding.container.safeDelayedClick {
                onItemClick(item)
            }
        }
    }
}
