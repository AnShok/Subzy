package com.anshok.subzy.presentation.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.R
import com.anshok.subzy.databinding.ItemAppIconBinding
import com.anshok.subzy.domain.model.AppIconStyle

class AppIconAdapter(
    private val styles: List<AppIconStyle>,
    private var selected: AppIconStyle,
    private val onClick: (AppIconStyle) -> Unit
) : RecyclerView.Adapter<AppIconAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAppIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(style: AppIconStyle) {
            binding.icon.setImageResource(getDrawableRes(style))
            binding.iconLabel.text = style.label
            binding.icon.alpha = if (style == selected) 1f else 0.4f
            binding.icon.setOnClickListener {
                val prev = selected
                selected = style
                notifyItemChanged(styles.indexOf(prev))
                notifyItemChanged(adapterPosition)
                onClick(style)
            }
        }

        private fun getDrawableRes(style: AppIconStyle): Int = when (style) {
            AppIconStyle.CLASSIC -> R.mipmap.ic_launcher_classic
            AppIconStyle.NEON_BLUE -> R.mipmap.ic_launcher_neon_blue
            AppIconStyle.NEON_RED -> R.mipmap.ic_launcher_neon_red
            AppIconStyle.GLITCH -> R.mipmap.ic_launcher_glitch
            AppIconStyle.LIGHT -> R.mipmap.ic_launcher_light
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAppIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = styles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(styles[position])
    }
}