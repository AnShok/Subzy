package com.anshok.subzy.util.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.R

class LogoViewHolder(
    itemView: View,
    private val onItemClick: (String?) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val logoImage: ImageView = itemView.findViewById(R.id.item_logo)
    private val titleText: TextView? = itemView.findViewById(R.id.position_title)

    fun bind(name: String?, logoUrl: String?) {
        titleText?.text = name ?: "Unknown"
        titleText?.isSelected = false
        titleText?.postDelayed({ titleText.isSelected = true }, 1500)

        bindLogo(logoUrl, logoImage)

        itemView.setOnClickListener {
            onItemClick(name)
        }
    }
}
