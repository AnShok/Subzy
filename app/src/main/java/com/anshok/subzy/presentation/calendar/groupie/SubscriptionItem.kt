package com.anshok.subzy.presentation.calendar.groupie

import android.view.View
import com.anshok.subzy.databinding.ItemSubscriptionBinding
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.util.CurrencyUtils
import com.anshok.subzy.util.adapter.bindLogo
import com.anshok.subzy.util.adapter.toLogo
import com.anshok.subzy.util.safeDelayedClick
import com.xwray.groupie.viewbinding.BindableItem

/**
 * Элемент подписки для отображения в календаре.
 *
 * Отображает название, цену и логотип подписки в виде карточки.
 * Поддерживает кликабельность через [onClick], передавая объект [Subscription].
 *
 * Используется в [GroupedSubscriptionSection] внутри календаря.
 *
 * @param subscription Объект подписки, который необходимо отобразить.
 * @param onClick Колбэк, вызываемый при нажатии на элемент.
 */

class SubscriptionItem(
    private val subscription: Subscription,
    private val onClick: (Subscription) -> Unit
) : BindableItem<ItemSubscriptionBinding>() {

    override fun getLayout(): Int = com.anshok.subzy.R.layout.item_subscription

    override fun initializeViewBinding(view: View) =
        ItemSubscriptionBinding.bind(view)

    override fun bind(viewBinding: ItemSubscriptionBinding, position: Int) {
        with(viewBinding) {
            positionTitle.text = subscription.name
            costTitle.text =
                CurrencyUtils.formatPrice(subscription.price, subscription.currencyCode)
            bindLogo(subscription.logoUrl?.toLogo(root.context), itemLogo)

            container.safeDelayedClick { onClick(subscription) }
        }
    }
}
