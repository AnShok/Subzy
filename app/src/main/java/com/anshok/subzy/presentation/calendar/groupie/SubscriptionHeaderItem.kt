package com.anshok.subzy.presentation.calendar.groupie

import android.view.View
import com.anshok.subzy.databinding.ItemSubscriptionGroupBinding
import com.xwray.groupie.viewbinding.BindableItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Заголовок группы подписок в календаре, отображающий дату.
 *
 * Используется в [GroupedSubscriptionSection] для визуального разделения подписок по дате.
 * Форматирует дату в стиле "5 апреля" и отображает её в [ItemSubscriptionGroupBinding.groupDateText].
 *
 * @param date Дата, к которой относится группа подписок.
 */
class SubscriptionHeaderItem(private val date: LocalDate) :
    BindableItem<ItemSubscriptionGroupBinding>() {

    override fun getLayout(): Int = com.anshok.subzy.R.layout.item_subscription_group

    override fun initializeViewBinding(view: View) =
        ItemSubscriptionGroupBinding.bind(view)

    override fun bind(viewBinding: ItemSubscriptionGroupBinding, position: Int) {
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
        viewBinding.groupDateText.text = date.format(formatter)
    }
}
