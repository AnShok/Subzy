package com.anshok.subzy.presentation.subDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.R

class SubscriptionDetailAdapter : RecyclerView.Adapter<SubscriptionDetailViewHolder>() {

    private val items = mutableListOf<Pair<String, String>>()

    fun submitList(newItems: List<Pair<String, String>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail, parent, false)
        return SubscriptionDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubscriptionDetailViewHolder, position: Int) {
        val (label, value) = items[position]
        holder.bind(label, value)
    }

    override fun getItemCount(): Int = items.size
}
