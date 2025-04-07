package com.anshok.subzy.presentation.subDetails.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.R

class SubscriptionDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val labelText: TextView = view.findViewById(R.id.label)
    private val valueText: TextView = view.findViewById(R.id.value)
    private val arrowIcon: ImageView = view.findViewById(R.id.arrow)

    fun bind(label: String, value: String) {
        labelText.text = label
        valueText.text = value
    }
}
