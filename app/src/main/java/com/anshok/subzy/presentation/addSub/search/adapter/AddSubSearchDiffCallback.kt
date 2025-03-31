package com.anshok.subzy.presentation.addSub.search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.anshok.subzy.domain.model.Logo

class AddSubSearchDiffCallback(
    private val oldList: List<Logo>,
    private val newList: List<Logo>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        val oldLogo = oldList[oldPos]
        val newLogo = newList[newPos]
        // Сравниваем по имени и домену
        return oldLogo.name == newLogo.name && oldLogo.domain == newLogo.domain
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos] == newList[newPos]
    }
}
