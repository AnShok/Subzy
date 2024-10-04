package com.anshok.subzy.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshok.subzy.databinding.ItemCategoryBinding

data class Category(val name: String, val iconRes: Int, val progressColorRes: Int)

class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClicked: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.title.text = category.name
            binding.itemLogo.setImageResource(category.iconRes)
            binding.progressBar.progressTintList =
                itemView.context.getColorStateList(category.progressColorRes)

            itemView.setOnClickListener {
                onItemClicked(category)
            }
        }
    }
}
