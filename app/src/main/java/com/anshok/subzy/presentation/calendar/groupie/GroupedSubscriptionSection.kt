package com.anshok.subzy.presentation.calendar.groupie

import com.xwray.groupie.Section

/**
 * Секция для отображения группы подписок, связанных с одной датой, на экране календаря.
 *
 * Включает в себя заголовок с датой ([SubscriptionHeaderItem]) и список элементов подписок ([SubscriptionItem]).
 * Используется адаптером [CalendarGroupieAdapter] для отображения подписок, сгруппированных по дате.
 *
 * @param dateHeader Заголовок группы — дата, к которой относятся подписки.
 * @param subscriptions Список подписок, относящихся к указанной дате.
 */
class GroupedSubscriptionSection(
    dateHeader: SubscriptionHeaderItem,
    subscriptions: List<SubscriptionItem>
) : Section() {

    init {
        setHeader(dateHeader)
        addAll(subscriptions)
    }
}
