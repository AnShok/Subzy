package com.anshok.subzy.presentation.calendar.groupie

import com.xwray.groupie.GroupieAdapter

/**
 * Адаптер для отображения подписок в календаре с использованием Groupie.
 *
 * Каждый день может содержать одну или несколько подписок, сгруппированных через [GroupedSubscriptionSection].
 * Используется в [com.anshok.subzy.presentation.calendar.CalendarFragment].
 *
 * Наследуется от [GroupieAdapter] и работает с элементами [SubscriptionItem] и заголовками [SubscriptionHeaderItem].
 */
class CalendarGroupieAdapter : GroupieAdapter()
